package vn.tech.website.store.controller.admin.user;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.admin.auth.AuthorizationController;
import vn.tech.website.store.controller.admin.common.CityDistrictCommuneController;
import vn.tech.website.store.controller.admin.common.UploadSingleImageController;
import vn.tech.website.store.dto.common.CityDistrictDto;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.model.Account;
import vn.tech.website.store.model.Province;
import vn.tech.website.store.repository.AccountRepository;
import vn.tech.website.store.repository.ProvinceRepository;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.EmailUtil;
import vn.tech.website.store.util.FacesUtil;
import vn.tech.website.store.util.StringUtil;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@Scope(value = "session")
@Getter
@Setter
public class MyAccountController {
    @Inject
    protected AuthorizationController authorizationController;
    @Inject
    private UploadSingleImageController uploadSingleImageController;
    @Inject
    private HttpServletRequest request;
    @Inject
    private CityDistrictCommuneController cityDistrictCommuneController;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProvinceRepository provinceRepository;

    private Account account;
    private AccountDto accountDto;
    private String emailBackup;
    private boolean allowSendCode;
    private String newEmail;
    private List<Account> accountList;
    private Date today;
    private List<SelectItem> listProvinceAccount;
    private List<Province> provinceList;

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            //init();
            resetAll();
        }
    }

    public void resetAll() {
        today = new Date();
        allowSendCode = false;
        newEmail = "";
        accountDto = new AccountDto();
        account = new Account();
        account =  accountRepository.findAccountByAccountId(authorizationController.getAccountDto().getAccountId());
//        account = accountRepository.findAccountByAccountId(1L);
        BeanUtils.copyProperties(account, accountDto);
        emailBackup = account.getEmail();
        accountList = new ArrayList<>();

        listProvinceAccount = new ArrayList<>();
        provinceList = (List<Province>) provinceRepository.findAll();
        for (Province dto : provinceList) {
            listProvinceAccount.add(new SelectItem(dto.getProvinceId(), dto.getName()));
        }
        cityDistrictCommuneController.resetAll();
        CityDistrictDto cityDistrictDto = new CityDistrictDto(accountDto.getProvinceId(), accountDto.getDistrictId(), accountDto.getCommuneId());
        cityDistrictCommuneController.setCityDistrictDto(cityDistrictDto);
        cityDistrictCommuneController.loadData();

        uploadSingleImageController.resetAll(null);
        if (accountDto.getImagePath() != null) {
            uploadSingleImageController.setImagePath(accountDto.getImagePath());
            uploadSingleImageController.setShowDeleteButton(true);
        } else {
            uploadSingleImageController.setImagePath(null);
        }
    }

    private boolean validate() {
        if (StringUtils.isBlank(accountDto.getFullName())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập họ và tên");
            return false;
        }
        if (accountDto.getGender() == null) {
            FacesUtil.addErrorMessage("Bạn vui lòng chọn giới tính");
            return false;
        }
        if (accountDto.getDateOfBirth() == null) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập ngày sinh");
            return false;
        }
        if (!accountDto.getDateOfBirth().before(new Date())) {
            FacesUtil.addErrorMessage("Ngày sinh phải nhỏ hơn ngày hiện tại");
            return false;
        }
        if (StringUtils.isBlank(accountDto.getPhone())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập số điện thoại");
            return false;
        }
        if (!accountDto.getPhone().matches("^0[1-9]{1}[0-9]{8,9}$|")) {
            FacesUtil.addErrorMessage("Số điện thoại không đúng định dạng");
            return false;
        }
        accountList = accountRepository.findAllAccountExpertId(authorizationController.getAccountDto().getAccountId());
        for (Account account : accountList) {
            if (accountDto.getPhone().equals(account.getPhone())) {
                FacesUtil.addErrorMessage("Số điện thoại đã tồn tại");
                return false;
            }
        }
        if (StringUtils.isBlank(accountDto.getAddress())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập địa chỉ");
            return false;
        }
        return true;
    }

    public void onSave() {
        if (validate()) {

            Account account = new Account();
            accountDto.setImagePath(uploadSingleImageController.getImagePath());
            accountDto.setProvinceId(cityDistrictCommuneController.getCityDistrictDto().getProvinceId());
            accountDto.setDistrictId(cityDistrictCommuneController.getCityDistrictDto().getDistrictId());
            accountDto.setCommuneId(cityDistrictCommuneController.getCityDistrictDto().getCommuneId());
            BeanUtils.copyProperties(accountDto, account);
            accountRepository.save(account);
            authorizationController.setAccountDto(accountDto);
            FacesUtil.addSuccessMessage("Cập nhật thành công.");
            resetAll();
        }
    }

    public boolean checkEmailChanged() {
        if (newEmail.equals(emailBackup)) {
            return false;
        }
        return true;
    }

    public void onSendVerifyCode() {
        accountDto.setVerifyCode(null);
        if (StringUtils.isBlank(newEmail)) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập email");
            return;
        }
        if (!newEmail.matches("^\\s*[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\\s*$")) {
            FacesUtil.addErrorMessage("Email không đúng định dạng");
            return;
        }
        if (!checkEmailChanged()) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập email mới");
            return;
        }
        if (!newEmail.equals(emailBackup)) {
            List<Account> accountList = accountRepository.getAllAccountExpertEmail(emailBackup);
            for (Account account : accountList) {
                if (newEmail.equals(account.getEmail())) {
                    FacesUtil.addErrorMessage("Email này đã tồn tại");
                    authorizationController.getAccountDto().setEmail(emailBackup);
                    return;
                }
            }
        }
        account.setVerifyCode(StringUtil.generateSalt());
        account = accountRepository.save(account);
        EmailUtil.getInstance().sendConfirmChangeEmail(newEmail, account.getUserName(),account.getVerifyCode(), Constant.ZEUS_SHOP, Constant.ZEUS_FOUNDER,Constant.URL_LOGO_ZEUS);
        FacesUtil.addSuccessMessage("Bạn vui lòng kiếm tra mail để lẩy mã xác nhận");
        allowSendCode = true;
    }

    public void onSaveEmail() {
        if(StringUtils.isBlank(accountDto.getVerifyCode())){
            FacesUtil.addErrorMessage("Bạn vui lòng kiểm tra email và nhập mã xác nhận");
            return;
        }
        if(!accountDto.getVerifyCode().equals(account.getVerifyCode())){
            FacesUtil.addErrorMessage("Mã xác nhận không đúng. Vui lòng kiểm tra lại");
            return;
        }
        account.setEmail(newEmail);
        account = accountRepository.save(account);
        FacesUtil.addSuccessMessage("Cập nhật thành công.");
        FacesUtil.closeDialog("changeEmailDialog");
        accountDto.setEmail(newEmail);
        allowSendCode = false;
        newEmail = "";
    }

    public void onCancelSaveEmail() {
        allowSendCode = false;
        newEmail = "";
    }
}
