package vn.tech.website.store.controller.admin.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.admin.common.CityDistrictCommuneController;
import vn.tech.website.store.controller.frontend.common.FacesNoticeController;
import vn.tech.website.store.crypto.AES;
import vn.tech.website.store.dto.common.CityDistrictDto;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.entity.EFunction;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.Account;
import vn.tech.website.store.model.FunctionRole;
import vn.tech.website.store.model.Province;
import vn.tech.website.store.model.Role;
import vn.tech.website.store.repository.*;
import vn.tech.website.store.util.*;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
@Named
@Scope(value = "session")
@Getter
@Setter
public class AuthorizationController implements Serializable {
    @Inject
    HttpServletRequest request;
    @Inject
    private AuthFunctionController authFunction;
    @Inject
    private CityDistrictCommuneController cityDistrictCommuneController;
    @Inject
    private FacesNoticeController facesNoticeController;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private FunctionRoleRepository functionRoleRepository;
    @Autowired
    private ProvinceRepository provinceRepository;

    private List<String> functions;
    private int role = DbConstant.ROLE_ID_NOT_LOGIN;
    private AccountDto accountDto;
    private Account account;
    private List<SelectItem> listProvinceAccount;
    private List<Province> provinceList;
    private List<Role> permissionList;
    private Date now;
    private String oldPassword;
    private String newPassword;
    private String newRepassword;
    private boolean married;
    // check show captcha
    private boolean showCaptcha;
    private Integer numberWrongShowCaptcha;
    private boolean requiredCaptcha;
    private int numberWrongPassword = 1;
    private Date plusDate;
    //	private static final Logger LOGGER = LoggerFactory.getLogger(AuthSocketFilter.class);
    private FilterConfig config = null;
    // message CAS
    private String MESSAGE_ERROR_GROWL;

    @Value("${LOGIN_SECRET_KEY}")
    private String cryptoSecretKey;

    @PostConstruct
    public void init() {
        // TODO: We have to check postback at here
        if (hasLogged()) {
            FacesUtil.redirect("/admin/dashboard.xhtml");
            return;
        }
        resetAll();
    }

    public void resetAll() {
        role = DbConstant.ROLE_ID_NOT_LOGIN;
        accountDto = new AccountDto();
        married = false;
        accountDto.setRoleId(role);
    }

    public void initChangePassword() {
        if (!hasLogged()) {
            FacesUtil.redirect("/admin/login.xhtml");
            return;
        }
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.oldPassword = "";
            this.newRepassword = "";
            this.newRepassword = "";
        }
    }

    public void initMyAccount() {
        if (!hasLogged()) {
            FacesUtil.redirect("/admin/login.xhtml");
            return;
        }
        if (!FacesContext.getCurrentInstance().isPostback()) {
            resetMyAccount();
        }
    }

    public void resetMyAccount() {
        account = accountRepository.findByAccountId(accountDto.getAccountId());
        BeanUtils.copyProperties(account, accountDto);
        listProvinceAccount = new ArrayList<>();
        now = new Date();
        provinceList = (List<Province>) provinceRepository.findAll();
        for (Province dto : provinceList) {
            listProvinceAccount.add(new SelectItem(dto.getProvinceId(), dto.getName()));
        }
        cityDistrictCommuneController.resetAll();
        CityDistrictDto cityDistrictDto = new CityDistrictDto(accountDto.getProvinceId(), accountDto.getDistrictId(), accountDto.getCommuneId());
        cityDistrictCommuneController.setCityDistrictDto(cityDistrictDto);
        cityDistrictCommuneController.loadData();
    }

    public void onUpload(FileUploadEvent e) {
        if (e.getFile().getSize() > Constant.MAX_FILE_SIZE) {
            setErrorForm("Dung lượng file quá lớn, dung lượng cho phép < " + Constant.MAX_FILE_SIZE + "MB");
            return;
        }
        accountDto.setImagePath(FileUtil.saveImageFile(e.getFile()));
    }

    private boolean validateField(AccountDto accountDto) {
        if (StringUtils.isBlank(accountDto.getFullName().trim())) {
            setErrorForm("Họ và tên là trường bắt buộc");
            return false;
        }

        if (accountDto.getDateOfBirth() != null) {
            if (accountDto.getDateOfBirth().getYear() <= -1899) {
                setErrorForm("Ngày sinh sai định dạng");
                return false;
            }
        }

        if (!accountDto.getPhone().matches("^0[1-9]{1}[0-9]{8,9}$|")) {
            setErrorForm("Số điện thoại không đúng định dạng");
            return false;
        }

        accountDto.setPhone(accountDto.getPhone().trim());
        Account oldPhone = accountDto.getPhone().compareTo("") != 0 ? accountRepository.findByPhone(accountDto.getPhone()) : null;
        if (oldPhone != null && !accountDto.getAccountId().equals(oldPhone.getAccountId())) {
            setErrorForm("Số điện thoại đã tồn tại");
            return false;
        }

        if (!accountDto.getEmail().matches("^\\s*[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\\s*$")) {
            setErrorForm("Email không đúng định dạng");
            return false;
        }

        if (StringUtils.isBlank(accountDto.getEmail())) {
            setErrorForm("Email là trường bắt buộc");
            return false;
        }

        if (!StringUtil.isValidEmailAddress(accountDto.getEmail())) {
            setErrorForm("Địa chỉ email không đúng định dạng");
            return false;
        }

        accountDto.setEmail(accountDto.getEmail().trim());
        Account oldEmail = accountRepository.findByEmail(accountDto.getEmail());
        if (oldEmail != null && !accountDto.getAccountId().equals(oldEmail.getAccountId())) {
            setErrorForm("Email đã tồn tại");
            FacesUtil.updateView("growl");
            return false;
        }
        return true;
    }

    public void onSaveData() {
        if (!validateField(accountDto)) {
            FacesUtil.updateView(Constant.ERROR_GROWL_ID);
            return;
        }
        accountDto.setProvinceId(cityDistrictCommuneController.getCityDistrictDto().getProvinceId());
        accountDto.setDistrictId(cityDistrictCommuneController.getCityDistrictDto().getDistrictId());
        accountDto.setCommuneId(cityDistrictCommuneController.getCityDistrictDto().getCommuneId());
        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);
        account.setUpdateBy(accountDto.getAccountId());
        accountRepository.save(account);
        setSuccessForm("Cập nhật thành công");

        // update view
        FacesUtil.updateView("topbar");
        FacesUtil.updateView(Constant.ERROR_GROWL_ID);
        resetMyAccount();
    }

    public void login() {
        String username = StringUtils.isBlank(accountDto.getUserName()) ? "" : AES.decrypt(accountDto.getUserName(), cryptoSecretKey);
        String password = StringUtils.isBlank(accountDto.getPassword()) ? "" : AES.decrypt(accountDto.getPassword(), cryptoSecretKey);

        createAccountDefault();

        if (StringUtils.isBlank(username.trim())) {
            setErrorForm("Bạn vui lòng nhập tên đăng nhập");
            return;
        }
        if ("".equals(password)) {
            setErrorForm("Bạn vui lòng nhập mật khẩu");
            return;
        }

        Account account = accountRepository.findAccountByUserName(username.trim());

        if (account == null) {
            setErrorForm("Tên đăng nhập hoặc mật khẩu không chính xác!");
            return;
        }
        if (account.getRoleId() == DbConstant.ROLE_ID_USER) {
            setErrorForm("Tên đăng nhập hoặc mật khẩu không chính xác!");
            return;
        }
        if (!StringUtil.encryptPassword(password, account.getSalt()).equals(account.getPassword())) {
            // Check to show the captcha
            if (showCaptcha) {
                int numToShowCaptcha = numberWrongShowCaptcha == null ? Constant.NUMBER_WRONG_PASSWORD_TO_SHOW_CAPTCHA : numberWrongShowCaptcha;
                if (numberWrongPassword++ >= numToShowCaptcha && !requiredCaptcha) {
                    requiredCaptcha = true;
                }
            }
            return;
        }

        if (DbConstant.ACCOUNT_INACTIVE_STATUS.equals(account.getStatus())) {
            setErrorForm("Tài khoản chưa được phê duyệt");
            return;
        }
        if (account.getStatus().equals(DbConstant.ACCOUNT_LOCK_STATUS)) {
            setErrorForm("Tài khoản của bạn đã bị khóa");
            return;
        }
        if (account.getStatus().equals(DbConstant.ACCOUNT_DELETE_STATUS)) {
            setErrorForm("Tài khoản này đã bị xóa");
            return;
        }

        // process login
        if (!processLogin(account)) {
            return;
        }

        // Redirect to home page
        FacesUtil.redirect("/admin/dashboard.xhtml");
    }

    public boolean processLogin(Account account) {
        Role userRole = roleRepository.findRoleByRoleId(account.getRoleId());
        if (userRole == null) {
            setErrorForm("Quyền hạn không đủ để đăng nhập");
            return false;
        }

        // reset
        requiredCaptcha = false;
        numberWrongPassword = 1;

        BeanUtils.copyProperties(account, accountDto);
        accountDto.setRoleName(userRole.getName());
        setRole(account.getRoleId());

        return true;
    }

    public void logout() {
        // destroy current session
        request.getSession().invalidate();
        // Redirect to home page
        FacesUtil.redirect("/admin/login.xhtml");
    }

    public AccountDto getAccountDto() {
        if (accountDto == null) {
            resetAll();
        }
        return accountDto;
    }

    public void onChangePassword() {
        if (oldPassword.equals("")) {
            setErrorForm("Bạn vui lòng nhập mật khẩu hiện tại");
            return;
        }
        if (newPassword.equals("")) {
            setErrorForm("Bạn vui lòng nhập mật khẩu mới");
            return;
        }
        if (newPassword.length() < DbConstant.ACCOUNT_MAXLENGTH_PASSWORD_ADMIN) {
            setErrorForm("Mật khẩu phải từ 12 ký tự gồm chữ cái, chữ số và ký tự đặc biệt");
            return;
        }

        if (!newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{12,}$")) {
            setErrorForm("Mật khẩu mới không đúng định dạng, phải gồm chữ cái, chữ số và ký tự đặc biệt");
            return;
        }
        if (StringUtils.isBlank(newRepassword.trim())) {
            setErrorForm("Bạn vui lòng nhập lại mật khẩu mới");
            return;
        }
        if (!newPassword.equals(newRepassword)) {
            setErrorForm("Mật khẩu mới và nhập lại mật khẩu không trùng khớp");
            return;
        }
        Account user = accountRepository.findAccountByAccountId(accountDto.getAccountId());
        if (user == null) {
            setErrorForm("Tài khoản của bạn đã không còn tồn tại");
            return;
        }
        if (!user.getPassword().equals(StringUtil.encryptPassword(oldPassword, user.getSalt()))) {
            setErrorForm("Mật khẩu hiện tại không đúng");
            return;
        }
        if (user.getPassword().equals(StringUtil.encryptPassword(newPassword, user.getSalt()))) {
            setErrorForm("Mật khẩu mới phải khác mật khẩu cũ");
            return;
        }
        user.setPassword(StringUtil.encryptPassword(newPassword, user.getSalt()));
        //user.setFirstTimeLogin(true);
        //user.setTimeToChangePassword(new Date());
        accountRepository.save(user);
        FacesUtil.addSuccessMessage(Constant.ERROR_MESSAGE, "Thay đổi mật khẩu thành công, Vui lòng đăng nhập lại");
        resetAll();
        FacesUtil.redirect("/admin/login.xhtml");
    }

    private void createAccountDefault() {
        String defaultAccount = PropertiesUtil.getProperty("default_account");
        String defaultUserName = PropertiesUtil.getProperty("default_username");
        String defaultPassword = PropertiesUtil.getProperty("default_password");
        if (accountRepository.findAccountByUserName(defaultUserName) == null) {
            Account user = new Account();
            user.setUserName(defaultUserName);
            user.setFullName("Admin");
            user.setDateOfBirth(new Date());
            user.setGender(DbConstant.MAN);
            user.setRoleId(DbConstant.ROLE_ID_ADMIN);
            user.setSalt(StringUtil.generateSalt());
            user.setPassword(StringUtil.encryptPassword(defaultPassword, user.getSalt()));
            user.setEmail(defaultAccount);
            user.setPhone("0999999999");
            user.setAddress("Ha Noi, Viet Nam");
            user.setStatus(DbConstant.ACCOUNT_ACTIVE_STATUS);
            user.setProvinceId(1L);
            user.setDistrictId(1L);
            user.setCommuneId(1L);
            user.setCreateDate(new Date());
            user.setUpdateDate(new Date());
            accountRepository.save(user);
        }
    }

    public boolean hasRole(EScope... scopes) {
        return authFunction.hasScope(scopes);
    }

    public EFunction getActions(EScope scope) {
        return authFunction.scope(scope);
    }

    public boolean hasLogged() {
        return role != DbConstant.ROLE_ID_NOT_LOGIN;
    }

    private void setErrorForm(String str) {
        FacesUtil.addErrorMessage(str);
    }

    private void setSuccessForm(String str) {
        FacesUtil.addSuccessMessage(str);
    }
}
