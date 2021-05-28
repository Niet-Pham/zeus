package vn.tech.website.store.controller.frontend.user;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.frontend.BaseFEController;
import vn.tech.website.store.controller.frontend.common.AddressFEController;
import vn.tech.website.store.controller.frontend.common.FacesNoticeController;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.jsf.GoogleRecaptcha;
import vn.tech.website.store.model.Account;
import vn.tech.website.store.model.Province;
import vn.tech.website.store.repository.AccountRepository;
import vn.tech.website.store.repository.ProvinceRepository;
import vn.tech.website.store.util.*;

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
public class AccountFEController extends BaseFEController {
    @Inject
    HttpServletRequest request;

    @Inject
    private AddressFEController addressFEController;

    @Inject
    private FacesNoticeController facesNoticeController;
    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    private Account account;
    private boolean married;
    private List<SelectItem> listProvinceAccount;
    private List<Province> provinceList;
    private AccountDto accountDto;
    private Date timeNow;
    private String link;
    private String convertDateOfBirth;
    private String convertDateOfIssue;

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAll();
        }
    }

    public void resetAll() {
        timeNow = new Date();
        account = new Account();
        accountDto = new AccountDto();
        //account.setOrg(false);
        married = false;
        addressFEController.resetAll();
        listProvinceAccount = new ArrayList<>();
        provinceList = (List<Province>) provinceRepository.findAll();
        for (Province dto : provinceList) {
            listProvinceAccount.add(new SelectItem(dto.getProvinceId(), dto.getName()));
        }

    }

    private boolean validate(AccountDto accountDto) {
//        if (accountDto.isOrg()) {
//
//            if (StringUtils.isBlank(accountDto.getOrgName())) {
//                setErrorForm("Tên tổ chức là trường bắt buộc");
//                return false;
//            }
//            if (StringUtils.isBlank(accountDto.getOrgPhone())) {
//                setErrorForm("Số điện thoại tổ chức là trường bắt buộc");
//                return false;
//            }
//            if (!accountDto.getOrgPhone().matches("[0]{1}[1-9]{1}[0-9]{8,9}")) {
//                setErrorForm("Số điện thoại tổ chức không đúng định dạng");
//                return false;
//            }
//            if (StringUtils.isBlank(accountDto.getFullName())) {
//                setErrorForm("Người đại diện là trường bắt buộc");
//                return false;
//            }
//            if (accountDto.getPhone().trim().equals(accountDto.getOrgPhone().trim())) {
//                setErrorForm("Số điện thoại của người đại diện không được trùng số điện thoại tổ chức");
//                return false;
//            }
//        }

        if (StringUtils.isBlank(accountDto.getPhone())) {
            setErrorForm("Số điện thoại là trường bắt buộc");
            return false;
        }
        if (!accountDto.getPhone().matches("[0]{1}[1-9]{1}[0-9]{8,9}")) {
            setErrorForm("Số điện thoại không đúng định dạng");
            return false;
        }

        if (StringUtils.isBlank(accountDto.getEmail())) {
            setErrorForm("Email là trường bắt buộc");
            return false;
        }
        if (!accountDto.getEmail().matches("^\\s*[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\\s*$")) {
            setErrorForm("Email không đúng định dạng");
            return false;
        }
//        if(accountDto.isOrg()) {
//            if (StringUtils.isBlank(accountDto.getUsername())) {
//                setErrorForm("Tên đăng nhập là trường bắt buộc");
//                return false;
//            }
//            if (accountDto.getUsername().length() < DbConstant.ACCOUNT_MINLENGTH_USERNAME) {
//                setErrorForm("Tên đăng nhập phải có ít nhất " + DbConstant.ACCOUNT_MINLENGTH_USERNAME + " ký tự");
//                return false;
//            }
//            if (!accountDto.getUsername().matches("^[\\w\\.]{6,50}$")) {
//                setErrorForm("Tên đăng nhập không đúng định dạng");
//                return false;
//            }
//        }

        if (accountDto.getPassword().equals("")) {
            setErrorForm("Mật khẩu là trường bắt buộc");
            return false;
        }

        if (accountDto.getPassword().length() < DbConstant.ACCOUNT_MINLENGTH_PASSWORD_USER) {
            setErrorForm("Mật khẩu phải có tối thiểu " + DbConstant.ACCOUNT_MINLENGTH_PASSWORD_USER + " ký tự gồm chữ cái, chữ số và ký tự đặc biệt");
            return false;
        }
        if (!accountDto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{12,}$")) {
            setErrorForm("Mật khẩu không đúng định dạng,Mật khẩu phải có tối thiểu " + DbConstant.ACCOUNT_MINLENGTH_PASSWORD_USER + " ký tự gồm chữ cái, chữ số và ký tự đặc biệt");
            return false;
        }

        if (accountDto.getRePassword().equals("")) {
            setErrorForm("Nhập lại mật khẩu là trường bắt buộc");
            return false;
        }
//        if(!accountDto.isOrg()) {
//            if (StringUtils.isBlank(accountDto.getUsername())) {
//                setErrorForm("Tên đăng nhập là trường bắt buộc");
//                return false;
//            }
//            if (accountDto.getUsername().length() < DbConstant.ACCOUNT_MINLENGTH_USERNAME) {
//                setErrorForm("Tên đăng nhập phải có ít nhất " + DbConstant.ACCOUNT_MINLENGTH_USERNAME + " ký tự");
//                return false;
//            }
//            if (!accountDto.getUsername().matches("^[\\w\\.]{6,50}$")) {
//                setErrorForm("Tên đăng nhập không đúng định dạng");
//                return false;
//            }
//        }
        if(addressFEController.getCityDistrictDto().getProvinceId() == null){
            setErrorForm("Bạn phải chọn tỉnh/thành phố");
            return false;
        }
        if(addressFEController.getCityDistrictDto().getDistrictId() == null){
            setErrorForm("Bạn phải chọn quận/huyện");
            return false;
        }

        if(addressFEController.getCityDistrictDto().getCommuneId() == null){
            setErrorForm("Bạn phải nhập phường/xã");
            return false;
        }

//        if (!accountDto.isOrg()) {
//
//            if (StringUtils.isBlank(accountDto.getFullName())) {
//                setErrorForm("Họ và tên là trường bắt buộc");
//                return false;
//            }
//        }
//        if(!accountDto.isOrg()) {
//            accountDto.setPhone(accountDto.getPhone().trim());
//            if (accountDto.getAccountId() == null) {
//                Account oldAccount = accountRepository.findByPhone(accountDto.getPhone());
//                if (oldAccount != null) {
//                    setErrorForm("Số điện thoại đã tồn tại");
//                    FacesUtil.updateView("growl");
//                    return false;
//                }
//                Account checkAccountOrg = accountRepository.findByOrgPhone(accountDto.getPhone());
//                if (checkAccountOrg != null) {
//                    setErrorForm("Số điện thoại đã tồn tại");
//                    FacesUtil.updateView("growl");
//                    return false;
//                }
//            }
//        }
//        if(accountDto.isOrg()) {
//            accountDto.setPhone(accountDto.getPhone().trim());
//            if (accountDto.getAccountId() == null) {
//                Account oldAccount = accountRepository.findByPhone(accountDto.getPhone());
//                if (oldAccount != null) {
//                    setErrorForm("Số điện thoại của người đại diện đã tồn tại");
//                    FacesUtil.updateView("growl");
//                    return false;
//                }
//                Account checkAccountOrg = accountRepository.findByOrgPhone(accountDto.getPhone());
//                if (checkAccountOrg != null) {
//                    setErrorForm("Số điện thoại của người đại diện đã tồn tại");
//                    FacesUtil.updateView("growl");
//                    return false;
//                }
//            }
//            accountDto.setOrgPhone(accountDto.getOrgPhone().trim());
//            if (accountDto.getAccountId() == null) {
//                Account oldAccount = accountRepository.findByPhone(accountDto.getOrgPhone());
//                if (oldAccount != null) {
//                    setErrorForm("Số điện thoại tổ chức đã tồn tại");
//                    FacesUtil.updateView("growl");
//                    return false;
//                }
//                Account checkAccountOrg = accountRepository.findByOrgPhone(accountDto.getOrgPhone());
//                if (checkAccountOrg != null) {
//                    setErrorForm("Số điện thoại tổ chức đã tồn tại");
//                    FacesUtil.updateView("growl");
//                    return false;
//                }
//            }
//        }

        if (accountDto.getPassword().compareTo(accountDto.getRePassword()) != 0) {
            setErrorForm("Mật khẩu và nhập lại mật khẩu không trùng khớp");
            return false;
        }

        if (!GoogleRecaptcha.verify()) {
            setErrorForm("Bạn vui lòng chọn mã xác nhận");
            return false;
        }

        accountDto.setUserName(accountDto.getUserName().trim());
        if (accountDto.getAccountId() == null) {
            Account oldAccount = accountRepository.findAccountByUserNameAndRoleId(accountDto.getUserName(), DbConstant.ROLE_ID_USER);
            if (oldAccount != null) {
                setErrorForm("Tên đăng nhập đã tồn tại");
                return false;
            }
        }
        accountDto.setEmail(accountDto.getEmail().trim());
        if (accountDto.getAccountId() == null) {
            Account oldAccount = accountRepository.findByEmail(accountDto.getEmail());
            if (oldAccount != null) {
                setErrorForm("Email đã tồn tại");
                return false;
            }
        }

        return true;
    }

    public void onSaveData(){
        resetGoogleRecaptchaForm();

        if (!validate(accountDto)) {
            return;
        }
        accountDto.setProvinceId(addressFEController.getCityDistrictDto().getProvinceId());
        accountDto.setDistrictId(addressFEController.getCityDistrictDto().getDistrictId());
        accountDto.setCommuneId(addressFEController.getCityDistrictDto().getCommuneId());
        accountDto.setSalt(StringUtil.generateSalt());
        String password = accountDto.getPassword();
        accountDto.setPassword(StringUtil.encryptPassword(password, accountDto.getSalt()));
        accountDto.setStatus(DbConstant.ACCOUNT_INACTIVE_STATUS);
        accountDto.setRoleId(DbConstant.ROLE_ID_USER);
//        accountDto.setFirstTimeLogin(true);
//        accountDto.setTimeToChangePassword(new Date());
////        accountDto.setFinishInfoStatus(false);
//        if (StringUtils.isBlank(accountDto.getRelativeName())) {
//           accountDto.setRelativeName(null);
//        }
//        if (StringUtils.isBlank(accountDto.getRelativeIdCardNumber())) {
//            accountDto.setRelativeIdCardNumber(null);
//        }
        BeanUtils.copyProperties(accountDto, account);
        account = accountRepository.save(account);
        //
        link = buildHttpURI() + "/frontend/account/activation.xhtml?token=" + "abcdefgh";
        EmailUtil.getInstance().sendCreateUserEmail(account.getEmail(), link, account.getUserName());
        //
        setSuccessForm("Tạo tài khoản thành công! vui lòng kiểm tra email để kích hoạt tài khoản");
        facesNoticeController.closeModal("RegisterPopup");
        resetAll();
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

//    public void onTabChange() {
//        account.setOrg(!account.isOrg());
//    }

    @Override
    protected String getMenuId() {
        return Constant.ID_QUAN_LY_TAI_KHOAN_NGUOI_DUNG;
    }
}
