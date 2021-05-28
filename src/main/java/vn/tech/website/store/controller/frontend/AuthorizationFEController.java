package vn.tech.website.store.controller.frontend;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.admin.BaseController;
import vn.tech.website.store.controller.frontend.common.AddressFEController;
import vn.tech.website.store.controller.frontend.common.FacesNoticeController;
import vn.tech.website.store.crypto.AES;
import vn.tech.website.store.dto.common.CityDistrictDto;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.dto.user.AccountSearchDto;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.jsf.CookieHelper;
import vn.tech.website.store.jsf.GoogleRecaptcha;
import vn.tech.website.store.model.Account;
import vn.tech.website.store.model.Role;
import vn.tech.website.store.repository.AccountRepository;
import vn.tech.website.store.repository.ProvinceRepository;
import vn.tech.website.store.repository.RoleRepository;
import vn.tech.website.store.util.*;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.*;

@Log4j2
@Named
@Scope(value = "session")
@Getter
@Setter
public class AuthorizationFEController implements Serializable {
    @Inject
    HttpServletRequest request;
    @Inject
    private FacesNoticeController facesNoticeController;
    @Inject
    private AddressFEController addressFEController;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProvinceRepository provinceRepository;

    private List<String> myMenus;
    private int role = DbConstant.ROLE_ID_NOT_LOGIN;
    private AccountDto accountDto;
    private Account account;

    // upload image
    private Map<String, String> accuracyCompanyFilePathMap;
    private boolean onChangeAccountType;

    private boolean married;
    private List<SelectItem> listProvinceAccount;
    private Date now;

    private String oldPassword;
    private String newPassword;
    private String newRepassword;
    private String inputCode;
    private String code;
    private boolean checkLostPass;
    private boolean resend;
    private boolean rememberLogin;
    private boolean checkActiveStatus;
    private String encodeEmail;
    private String link;

    private boolean showCaptcha;
    private Integer numberWrongShowCaptcha;
    private boolean requiredCaptcha;
    private int numberWrongPassword = 1;
    private boolean check;
    private FilterConfig config = null;
    private Part avatarImagePart;
    private List<SelectItem> apiSexList;

    private String verifyCodeRegister;
    private boolean isFirstClick = true;
    private String emailBak;

    @Value("${LOGIN_SECRET_KEY}")
    private String cryptoSecretKey;

    @PostConstruct
    public void init() {
        // TODO: We have to check postback at herea
        updateMenuByRole();
        resetAll();
    }

    public void initLoginModal() {
        resetAll();
    }

    public void initInfoAccount() {
        if (!hasLogged()) {
            FacesUtil.redirect("/frontend/index2.xhtml");
            return;
        }
        if (!FacesContext.getCurrentInstance().isPostback()) {
            resetMyAccount();
            check = false;
        }
    }

    public void resetMyAccount() {
        account = accountRepository.findByAccountId(accountDto.getAccountId());
        BeanUtils.copyProperties(account, accountDto);
        addressFEController.resetAll();
        CityDistrictDto cityDistrictDto = new CityDistrictDto(accountDto.getProvinceId(), accountDto.getDistrictId(), accountDto.getCommuneId());
        addressFEController.setCityDistrictDto(cityDistrictDto);
        addressFEController.loadData();
        listProvinceAccount = new ArrayList<>();
        oldPassword = null;
        newPassword = null;
        newRepassword = null;
    }

    private boolean validateUploadFile(FileUploadEvent e) {
        if (!FileUtil.isAcceptFileType(e.getFile())) {
            facesNoticeController.addErrorMessage("Loại file không được phép. Những file được phép: " + FileUtil.getAcceptFileImageString().replaceAll(",", ", ").toUpperCase());
            FacesUtil.updateView(Constant.ERROR_FE_GROWL_ID);
            return false;
        }
        if (e.getFile().getSize() > Constant.MAX_FILE_SIZE) {
            facesNoticeController.addErrorMessage("Dung lượng file quá lớn. Dung lượng tối đa " + Constant.MAX_FILE_SIZE / 1000000 + "Mb");
            FacesUtil.updateView(Constant.ERROR_FE_GROWL_ID);
            return false;
        }
        return true;
    }

    public void onUploadAvatar(FileUploadEvent e) {
        try {
            if (!validateUploadFile(e)) {
                return;
            }
            accountDto.setImagePath(null);
            accountDto.setImagePath(FileUtil.saveImageFile(e.getFile()));
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }

    public void onUploadMultipleFile(FileUploadEvent e) {
        try {
            if (!validateUploadFile(e)) {
                return;
            }
            if (accuracyCompanyFilePathMap.containsKey(e.getFile().getFileName())) {
                facesNoticeController.addErrorMessage("Ảnh đã tồn tại, vui lòng chọn ảnh khác");
                FacesUtil.updateView(Constant.ERROR_FE_GROWL_ID);
                return;
            }
            if (accuracyCompanyFilePathMap.size() >= 4) {
                facesNoticeController.addErrorMessage("Bạn chỉ được tải lên tối đa 4 ảnh giấy phép ĐKKD");
                FacesUtil.updateView(Constant.ERROR_FE_GROWL_ID);
                return;
            }
            String fileName = e.getFile().getFileName();
            String filePath = FileUtil.saveFile(e.getFile());
            accuracyCompanyFilePathMap.put(fileName, filePath);
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }

    public void onRemoveFile(String key) {
        accuracyCompanyFilePathMap.remove(key);
    }

    public boolean validateProfile() {
        if (StringUtils.isBlank(accountDto.getFullName())) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập họ và tên");
            return false;
        }
        if (accountDto.getDateOfBirth() == null) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập ngày sinh");
            return false;
        }
        if (!accountDto.getDateOfBirth().before(new Date())) {
            facesNoticeController.addErrorMessage("Ngày sinh phải nhỏ hơn ngày hiện tại");
            return false;
        }
        if (StringUtils.isBlank(accountDto.getPhone())) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập số điện thoại");
            return false;
        }
        if (!accountDto.getPhone().matches("^0[1-9]{1}[0-9]{8,9}$|")) {
            facesNoticeController.addErrorMessage("Số điện thoại không đúng định dạng");
            return false;
        }
        if (StringUtils.isBlank(accountDto.getAddress())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập địa chỉ");
            return false;
        }

        return true;
    }

    public void onSaveData() throws ScriptException {
        if (!validateProfile()) {
            return;
        }
        accountDto.setProvinceId(addressFEController.getCityDistrictDto().getProvinceId());
        accountDto.setDistrictId(addressFEController.getCityDistrictDto().getDistrictId());
        accountDto.setCommuneId(addressFEController.getCityDistrictDto().getCommuneId());
        if (check) {
            if (!onChangePassword()) {
                return;
            }
        }
        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);
        account.setUpdateBy(accountDto.getAccountId());
        account.setUpdateDate(new Date());
        accountRepository.save(account);

        resetMyAccount();
        check = false;

        FacesUtil.redirect("/frontend/account/profile.xhtml");
        facesNoticeController.addSuccessMessage("Cập nhật thành công");
        FacesUtil.updateView(Constant.ERROR_FE_GROWL_ID);
    }

    public void initChangePassword() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            this.oldPassword = "";
            this.newRepassword = "";
            this.newRepassword = "";
        }
    }

    public boolean hasRole(String menuId) {
        return myMenus.indexOf(menuId) > -1;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
        updateMenuByRole();
    }

    public void login() {
        isFirstClick = true;
        String username = StringUtils.isBlank(accountDto.getUserName()) ? "" : AES.decrypt(accountDto.getUserName(), cryptoSecretKey);
        String password = StringUtils.isBlank(accountDto.getPassword()) ? "" : AES.decrypt(accountDto.getPassword(), cryptoSecretKey);

        if (StringUtils.isBlank(username.trim())) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập tên đăng nhập");
            return;
        }
        if ("".equals(password)) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập mật khẩu");
            return;
        }

        AccountSearchDto searchDto = new AccountSearchDto(DbConstant.ROLE_ID_USER, username.trim());
        List<AccountDto> accounts =  accountRepository.search(searchDto);
        if (accounts.isEmpty()) {
            facesNoticeController.addErrorMessage("Tên đăng nhập hoặc mật khẩu không chính xác");
            return;
        }

        AccountDto account = accountRepository.search(searchDto).get(0);
        if (account == null) {
            facesNoticeController.addErrorMessage("Tên đăng nhập hoặc mật khẩu không chính xác");
            return;
        }
        if (account.getRoleId() != DbConstant.ROLE_ID_USER) {
            facesNoticeController.addErrorMessage("Tên đăng nhập hoặc mật khẩu không chính xác");
            return;
        }

        if (!StringUtil.encryptPassword(password, account.getSalt()).equals(account.getPassword())) {
            return;
        }

        if (account.getStatus().equals(DbConstant.ACCOUNT_LOCK_STATUS)) {
            facesNoticeController.addErrorMessage("Tài khoản đang bị khóa");
            return;
        }
        BeanUtils.copyProperties(account, accountDto);

        //PROCESS LOGIN
        processLogin(account);
        if (rememberLogin) {
            //save to cookie
            CookieHelper.setCookie(Constant.COOKIE_ACCOUNT, account.getUserName());
            CookieHelper.setCookie(Constant.COOKIE_PASSWORD, StringUtil.encryptPassword(account.getPassword(), account.getSalt() + account.getCreateDate()));
        }
        facesNoticeController.closeModal("login-modal");
        // Redirect to home page
        facesNoticeController.reload();
        facesNoticeController.addSuccessMessage("Đăng nhập thành công!");
    }

    public void processLogin(Account account) {
        BeanUtils.copyProperties(account, accountDto);
        Role userRole = roleRepository.findRoleByRoleId(accountDto.getRoleId());
        accountDto.setRoleName(userRole.getName());

        this.role = account.getRoleId();
//        updateMenuByRole();
    }

    public void logout() {
        resetAll();

        // destroy current session
        request.getSession().invalidate();

        //remove cookie
        CookieHelper.removeCookie(Constant.COOKIE_ACCOUNT);
        CookieHelper.removeCookie(Constant.COOKIE_PASSWORD);

        // Redirect to home page
        FacesUtil.redirect("/frontend/index2.xhtml");
    }

    public List<String> getMyMenus() {
        return myMenus;
    }

    public void setMyMenus(List<String> myMenus) {
        this.myMenus = myMenus;
    }

    private void updateMenuByRole() {
        myMenus = new ArrayList<String>();
        myMenus.add(Constant.ID_CAU_HINH_TIN_TUC);
        // Menu trang chu
        switch (role) {
            case DbConstant.ROLE_ID_NOT_LOGIN:
                myMenus.add(Constant.ID_INDEX_FONTEND);
                myMenus.add(Constant.ID_ASSET_FRONTEND);
                myMenus.add(Constant.ID_QUAN_LY_TAI_KHOAN_NGUOI_DUNG);
                myMenus.add(Constant.ID_REGULATION_FRONTEND);
                myMenus.add(Constant.ID_REGULATION_DETAIL_FRONTEND);
                myMenus.add(Constant.ID_ASSET_DETAIL_FRONTEND);
                myMenus.add(Constant.ID_PAYMENT_WALLET_RETURN_URL);
                break;
            case DbConstant.ROLE_ID_USER:
                myMenus.add(Constant.ID_INDEX_FONTEND);
                myMenus.add(Constant.ID_PROPERTY_SALE_HISTORY);
                myMenus.add(Constant.ID_ASSET_FRONTEND);
                myMenus.add(Constant.ID_REGULATION_FRONTEND);
                myMenus.add(Constant.ID_QUAN_LY_TAI_KHOAN_NGUOI_DUNG);
                myMenus.add(Constant.ID_REGULATION_DETAIL_FRONTEND);
                myMenus.add(Constant.ID_MY_AUCTION);
                myMenus.add(Constant.ID_MY_PAYMENT);
                myMenus.add(Constant.ID_ASSET_DETAIL_FRONTEND);
                myMenus.add(Constant.ID_PAYMENT_WALLET_RETURN_URL);
                break;
            default:
                break;
        }
    }

    public void resetAll() {
        code = StringUtil.generateSalt();
        checkActiveStatus = false;
        role = DbConstant.ROLE_ID_NOT_LOGIN;
        rememberLogin = false;
        accountDto = new AccountDto();
        myMenus = new ArrayList<>();
        account = new Account();
        accountDto.setRoleId((role));
        accuracyCompanyFilePathMap = new LinkedHashMap<>();
        updateMenuByRole();
    }

    public AccountDto getAccountDto() {
        if (accountDto == null) {
            resetAll();
        }
        return accountDto;
    }

    public void showChangePassword() {
        check = !check;
    }

    public boolean onChangePassword() {
        if (StringUtils.isBlank(oldPassword)) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập mật khẩu hiện tại");
            return false;
        }
        if (StringUtils.isBlank(newPassword)) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập mật khẩu mới");
            return false;
        }
        if (oldPassword.equals(newPassword)) {
            facesNoticeController.addErrorMessage("Mật khẩu mới không được giống mật khẩu hiện tại");
            return false;
        }

        if (newPassword.length() < DbConstant.ACCOUNT_MINLENGTH_PASSWORD_USER) {
            facesNoticeController.addErrorMessage("Mật khẩu phải có tối thiểu 6 ký tự.");
            return false;
        }
        if (StringUtils.isBlank(newRepassword)) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập nhập lại mật khẩu");
            return false;
        }
        if (!newPassword.equals(newRepassword)) {
            facesNoticeController.addErrorMessage("Mật khẩu mới và nhập lại mật khẩu không giống nhau");
            return false;
        }

        Account user = accountRepository.findAccountByAccountId(accountDto.getAccountId());
        if (user == null) {
            facesNoticeController.addErrorMessage("Tài khoản của bạn đã không còn tồn tại");
            return false;
        }
        if (!user.getPassword().equals(StringUtil.encryptPassword(oldPassword, user.getSalt()))) {
            facesNoticeController.addErrorMessage("Mật khẩu hiện tại không đúng");
            return false;
        }
        if (user.getPassword().equals(StringUtil.encryptPassword(newPassword))) {
            facesNoticeController.addErrorMessage("Mật khẩu mới phải khác mật khẩu cũ");
            return false;
        }
        accountDto.setPassword(StringUtil.encryptPassword(newPassword, user.getSalt()));
        return true;
    }

    public void onCheckEmail() {
        if (StringUtils.isBlank(accountDto.getEmail().trim())) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập email đã đăng ký");
            return;
        }
        if (!accountDto.getEmail().trim().matches("^\\s*[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\\s*$")) {
            facesNoticeController.addErrorMessage("Email không đúng định dạng");
            return;
        }
        account = accountRepository.findAccountByEmail(accountDto.getEmail().trim());
        if (account == null || account.getRoleId() != DbConstant.ROLE_ID_USER) {
            facesNoticeController.addErrorMessage("Email không tồn tại");
            return;
        }
        code = StringUtil.generateSalt();
        EmailUtil.getInstance().sendLostPasswordEmail(account.getEmail(), true, Constant.ZEUS_SHOP, account.getUserName(), code, Constant.ZEUS_FOUNDER, null, null, Constant.URL_LOGO_ZEUS);
        checkLostPass = true;
        facesNoticeController.addSuccessMessage("Mã xác nhận đã được gửi về tài khoản email của bạn.");
    }

    public void onChangePasswordByCode() {
        if (StringUtils.isBlank(inputCode)) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập mã xác thực");
            return;
        }
        if (!code.equals(inputCode.trim())) {
            facesNoticeController.addErrorMessage("Mã xác nhận không chính xác, vui lòng nhập lại mã xác thực");
            return;
        }

        String newPassword = StringUtil.generatePassword();
        EmailUtil.getInstance().sendLostPasswordEmail(account.getEmail(), false, Constant.ZEUS_SHOP, account.getUserName(), code, Constant.ZEUS_FOUNDER, account.getEmail(), newPassword, Constant.URL_LOGO_ZEUS);
        Account accountUpdate = new Account();
        BeanUtils.copyProperties(account, accountUpdate);
        accountUpdate.setPassword(StringUtil.encryptPassword(newPassword, account.getSalt()));
        accountRepository.save(accountUpdate);

        code = "";
        account = new Account();
        accountDto = new AccountDto();
        facesNoticeController.addSuccessMessage("Đặt lại mật khẩu thành công, vui lòng sử dụng mật khẩu mới được gửi đến email của bạn.");
        facesNoticeController.closeModal("lost-pass-modal");
        facesNoticeController.openModal("login-modal");
    }

    public void onCreateNewPassword() {
        if (newPassword.equals("")) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập mật khẩu mới");
            return;
        }

        if (newRepassword.equals("")) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập lại mật khẩu mới");
            return;
        }
        if (newPassword.length() < DbConstant.ACCOUNT_MINLENGTH_PASSWORD_USER) {
            facesNoticeController.addErrorMessage("Mật khẩu phải có tối thiểu 12 ký tự");
            return;
        }
        if (!newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{12,}$")) {
            facesNoticeController.addErrorMessage("Mật khẩu không đúng định dạng");
            return;
        }
        if (!newPassword.equals(newRepassword)) {
            facesNoticeController.addErrorMessage("Mật khẩu mới và nhắc lại mật khẩu không giống nhau");
            return;
        }
        account.setPassword(StringUtil.encryptPassword(newPassword, account.getSalt()));
        accountRepository.save(account);
        facesNoticeController.addSuccessMessage("Lấy lại mật khẩu thành công");
        facesNoticeController.closeModal("CreateNewPassword");
        facesNoticeController.openModal("loginPopup");
    }

    public void sendActiveEmail() {
//        if (StringUtils.isBlank(accountDto.getUsername())) {
//            facesNoticeController.addErrorMessage("Bạn vui lòng nhập tài khoản");
//            return;
//        }
//        if (StringUtils.isBlank(accountDto.getPassword())) {
//            facesNoticeController.addErrorMessage("Bạn vui lòng nhập mật khẩu");
//            return;
//        }

        Account account = accountRepository.findAccountByUserName("abc");
//        if (account == null) {
//            facesNoticeController.addErrorMessage("Tài khoản hoặc mật khẩu không chính xác");
//            return;
//        }
//        if (account.getRoleId() != DbConstant.ROLE_ID_USER) {
//            facesNoticeController.addErrorMessage("Tài khoản hoặc mật khẩu không chính xác");
//            return;
//        }
        accountRepository.save(account);
        link = buildHttpURI() + "/frontend/account/activation.xhtml?token=" + "abcdefgh";
        EmailUtil.getInstance().sendCreateUserEmail(account.getEmail(), link, account.getUserName());
        facesNoticeController.addSuccessMessage("Email đã được gửi về tài khoản: " + encodeEmail + " vui lòng kiểm tra email để kích hoạt tài khoản");
        checkActiveStatus = false;
    }

    private String buildHttpURI() {
        String httpUrl = String.format("%s://%s:%s%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                request.getContextPath()
        );
        return httpUrl;
    }

    public void register() {
        String username = StringUtils.isBlank(accountDto.getUserName()) ? "" : AES.decrypt(accountDto.getUserName(), cryptoSecretKey);
        String password = StringUtils.isBlank(accountDto.getPassword()) ? "" : AES.decrypt(accountDto.getPassword(), cryptoSecretKey);
        String rePassword = StringUtils.isBlank(accountDto.getRePassword()) ? "" : AES.decrypt(accountDto.getRePassword(), cryptoSecretKey);

        if (StringUtils.isBlank(accountDto.getFullName())) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập họ tên");
            return;
        }
        if (StringUtils.isBlank(username)) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập tên đăng nhập");
            return;
        }
        Account account = accountRepository.findAccountByUserNameAndRoleId(username, DbConstant.ROLE_ID_USER);
        if (account != null) {
            facesNoticeController.addErrorMessage("Tên đăng nhập đã tồn tại");
            return;
        }
        if (StringUtils.isBlank(password)) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập mật khẩu");
            return;
        }
        if (password.length() < Constant.ACCOUNT_MINLENGTH_PASSWORD_USER) {
            facesNoticeController.addErrorMessage("Mật khẩu phải có ít nhất " + Constant.ACCOUNT_MINLENGTH_PASSWORD_USER + " ký tự");
            return;
        }

        if (StringUtils.isBlank(rePassword)) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập lại mật khẩu");
            return;
        }
        if (!rePassword.equals(password)) {
            facesNoticeController.addErrorMessage("Mật khẩu nhập lại không trùng khớp");
            return;
        }
        if (StringUtils.isBlank(accountDto.getPhone())) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập số điện thoại");
        }
        if (!accountDto.getPhone().matches("^0[1-9]{1}[0-9]{8,9}$|")) {
            facesNoticeController.addErrorMessage("Số điện thoại không đúng định dạng");
        }
        if (StringUtils.isBlank(accountDto.getEmail())) {
            facesNoticeController.addErrorMessage("Bạn vui lòng nhập email");
            return;
        }
        if (!accountDto.getEmail().matches("^\\s*[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\\s*$")) {
            facesNoticeController.addErrorMessage("Email không đúng định dạng");
            return;
        }
        accountDto.setEmail(accountDto.getEmail().trim());
        account = accountRepository.findAccountByEmailAndRoleId(accountDto.getEmail().trim(), DbConstant.ROLE_ID_USER);
        if (account != null) {
            facesNoticeController.addErrorMessage("Email đăng ký đã tồn tại");
            return;
        }
        if (isFirstClick) {
            emailBak = accountDto.getEmail();
            isFirstClick = false;
            verifyCodeRegister = StringUtil.generateSalt();
            EmailUtil.getInstance().sendVerifyCodeToRegister(accountDto.getEmail(), accountDto.getFullName(), verifyCodeRegister, Constant.ZEUS_SHOP, Constant.ZEUS_FOUNDER, Constant.URL_LOGO_ZEUS);
            facesNoticeController.addSuccessMessage("Bạn vui lòng kiểm tra email và nhập mã xác nhận");
            facesNoticeController.showVerifyRegister();
            return;
        }
        if (!isFirstClick) {
            if (!accountDto.getEmail().equals(emailBak)) {
                verifyCodeRegister = StringUtil.generateSalt();
                EmailUtil.getInstance().sendVerifyCodeToRegister(accountDto.getEmail(), accountDto.getFullName(), verifyCodeRegister, Constant.ZEUS_SHOP, Constant.ZEUS_FOUNDER, Constant.URL_LOGO_ZEUS);
            }
            if (StringUtils.isBlank(accountDto.getVerifyCode())) {
                facesNoticeController.addErrorMessage("Bạn vui lòng kiểm tra email và nhập mã xác nhận");
                return;
            }
            if (!accountDto.getVerifyCode().equals(verifyCodeRegister)) {
                facesNoticeController.addErrorMessage("Mã xác nhận không khớp");
                return;
            }

            Account accountRegister = new Account();
            accountRegister.setFullName(accountDto.getFullName());
            accountRegister.setUserName(username);
            accountRegister.setRoleId(DbConstant.ROLE_ID_USER);
            accountRegister.setSalt(StringUtil.generateSalt());
            accountRegister.setPassword(StringUtil.encryptPassword(password, accountRegister.getSalt()));
            accountRegister.setEmail(accountDto.getEmail().trim());
            accountRegister.setStatus(DbConstant.ACCOUNT_ACTIVE_STATUS);
            accountRepository.save(accountRegister);
            resetAll();
            facesNoticeController.closeModal("register-modal");
            // Redirect to home page
            facesNoticeController.reload();
            facesNoticeController.openModal("login-modal");
            facesNoticeController.addSuccessMessage("Đăng ký thành công!");
        }
    }

    public void setAccountDto(AccountDto accountDto) {
        this.accountDto = accountDto;
    }

    public String upperCaseFirstChar(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public boolean hasLogged() {
        return role != DbConstant.ROLE_ID_NOT_LOGIN;
    }
}
