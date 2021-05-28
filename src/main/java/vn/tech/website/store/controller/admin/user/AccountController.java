package vn.tech.website.store.controller.admin.user;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.admin.BaseController;
import vn.tech.website.store.controller.admin.auth.AuthorizationController;
import vn.tech.website.store.controller.admin.common.CityDistrictCommuneController;
import vn.tech.website.store.controller.frontend.common.AddressFEController;
import vn.tech.website.store.dto.common.CityDistrictDto;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.dto.user.AccountSearchDto;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.Account;
import vn.tech.website.store.model.Province;
import vn.tech.website.store.repository.AccountRepository;
import vn.tech.website.store.repository.ProvinceRepository;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.EmailUtil;
import vn.tech.website.store.util.FacesUtil;
import vn.tech.website.store.util.StringUtil;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
@Scope(value = "session")
@Getter
@Setter
public class AccountController extends BaseController {
    @Inject
    protected AuthorizationController authorizationController;
    @Inject
    private CityDistrictCommuneController cityDistrictCommuneController;
    @Inject
    private AddressFEController addressFEController;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    private ProvinceRepository provinceRepository;

    private boolean publicRegister; // true public
    private Account account;
    private LazyDataModel<AccountDto> lazyDataModel;
    private AccountSearchDto accountSearchDto;
    private List<SelectItem> accountList;
    private AccountDto objBackup;
    private boolean married;
    private List<SelectItem> listProvinceAccount;
    private LazyDataModel<AccountDto> accountDtos;
    private Province province;
    private AccountDto accountDto;
    private AccountDto accountDtoAddress;
    private Integer tabindex;
    private Date now;
    private final String ROLE_NAME_USER = "người dùng";
    private boolean showValidate;
    private String filePath;
    private List<SelectItem> apiSexList;

    public void initData(boolean publicRegister) {
        this.publicRegister = publicRegister;
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAll();
        }
    }

    public void resetAll() {
        filePath = null;
        showValidate = false;
        now = new Date();
        tabindex = 0;
        accountList = new ArrayList<>();
        accountDto = new AccountDto();
        accountSearchDto = new AccountSearchDto();
        married = false;
        listProvinceAccount = new ArrayList<>();
        cityDistrictCommuneController.resetAll();
        onSearch();
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
    }

    private boolean validate() {
//        if(!accountDto.isOrg()) {
//            if (accountDto.getDateOfBirth() == null) {
//                setErrorForm("Bạn vui lòng chọn ngày tháng năm sinh");
//                FacesUtil.updateView("growl");
//                return false;
//            }
//            if (accountDto.getDateOfIssue().getTime() <= accountDto.getDateOfBirth().getTime()) {
//                setErrorForm("Ngày cấp CMND/CCCD phải lớn hơn ngày sinh");
//                FacesUtil.updateView("growl");
//                return false;
//            }
//            if (married) {
//                if (StringUtils.isBlank(accountDto.getRelativeName())) {
//                    setErrorForm("Bạn vui lòng nhập tên vợ/chồng");
//                    FacesUtil.updateView("growl");
//                    return false;
//                }
//                if (StringUtils.isBlank(accountDto.getRelativeIdCardNumber())) {
//                    setErrorForm("Bạn vui lòng nhập số CMND/CCCD vợ/chồng");
//                    FacesUtil.updateView("growl");
//                    return false;
//                }
//                if (!accountDto.getRelativeIdCardNumber().matches("^[0-9]{9}(?:[0-9]{3})?$|")) {
//                    setErrorForm("Số CMND/CCCD vợ/chồng không đúng định dạng");
//                    FacesUtil.updateView("growl");
//                    return false;
//                }
//                if (accountDto.getIdCardNumber().compareToIgnoreCase(accountDto.getRelativeIdCardNumber()) == 0) {
//                    setErrorForm("Số CMND/CCCD của vợ/chồng không được trùng nhau");
//                    FacesUtil.updateView("growl");
//                    return false;
//                }
//            }
//        }
//        if (StringUtils.isBlank(accountDto.getFullName())) {
//            setErrorForm("Bạn vui lòng nhập họ và tên");
//            FacesUtil.updateView("growl");
//            return false;
//        }
        if (StringUtils.isBlank(accountDto.getPhone())) {
            setErrorForm("Bạn vui lòng nhập số điện thoại");
            FacesUtil.updateView("growl");
            return false;
        }
        if (StringUtils.isBlank(accountDto.getEmail())) {
            setErrorForm("Bạn vui lòng nhập email");
            FacesUtil.updateView("growl");
            return false;
        }
        if (!StringUtil.isValidEmailAddress(accountDto.getEmail())) {
            setErrorForm("Email không đúng định dạng");
            FacesUtil.updateView("growl");
            return false;
        }
        if (StringUtils.isBlank(accountDto.getUserName())) {
            setErrorForm("Bạn vui lòng nhập tên đăng nhập");
            FacesUtil.updateView("growl");
            return false;
        }
        if (accountDto.getGender() == null) {
            setErrorForm("Bạn vui lòng chọn giới tính");
            FacesUtil.updateView("growl");
            return false;
        }
//        if (StringUtils.isBlank(accountDto.getIdCardNumber())) {
//            setErrorForm("Bạn vui lòng nhập số CMND/CCCD");
//            FacesUtil.updateView("growl");
//            return false;
//        }
//        if (!accountDto.getIdCardNumber().matches("^[0-9]{9}(?:[0-9]{3})?$|")) {
//            setErrorForm("Số CMND/CCCD không đúng định dạng");
//            FacesUtil.updateView("growl");
//            return false;
//        }
//        if (accountDto.getProvinceIdOfIssue() == null) {
//            setErrorForm("Bạn vui lòng chọn nơi cấp CMND/CCCD");
//            FacesUtil.updateView("growl");
//            return false;
//        }
//        if (accountDto.getDateOfIssue() == null) {
//            setErrorForm("Bạn vui lòng nhập ngày cấp CMND/CCCD");
//            FacesUtil.updateView("growl");
//            return false;
//        }
//        if (StringUtils.isBlank(accountDto.getPermanentResidence())) {
//            setErrorForm("Bạn vui lòng nhập hộ khẩu thường trú");
//            FacesUtil.updateView("growl");
//            return false;
//        }
        accountDto.setUserName(accountDto.getUserName().trim());
        if (accountDto.getAccountId() == null || !objBackup.getUserName().equalsIgnoreCase(accountDto.getUserName().trim())) {
            Account oldAccount = accountRepository.findAccountByUserNameAndRoleId(accountDto.getUserName(), DbConstant.ROLE_ID_USER);
            if (oldAccount != null) {
                setErrorForm("Tên đăng nhập đã tồn tại");
                FacesUtil.updateView("growl");
                return false;
            }
        }
        accountDto.setEmail(accountDto.getEmail().trim());
        if (accountDto.getAccountId() == null || !objBackup.getEmail().equalsIgnoreCase(accountDto.getEmail().trim())) {
            Account oldAccount = accountRepository.findByEmail(accountDto.getEmail());
            if (oldAccount != null) {
                setErrorForm("Email đã tồn tại");
                FacesUtil.updateView("growl");
                return false;
            }
        }
        //accountDto.setIdCardNumber(accountDto.getIdCardNumber().trim());
//        if (accountDto.getAccountId() == null || objBackup.getIdCardNumber() == null || !objBackup.getIdCardNumber().equalsIgnoreCase(accountDto.getIdCardNumber().trim())) {
//            Account oldAccount = accountRepository.findByIdCardNumber(accountDto.getIdCardNumber());
//            if (oldAccount != null) {
//                setErrorForm("Số CMND/CCCD đã tồn tại");
//                FacesUtil.updateView("growl");
//                return false;
//            }
//        }
//        if(!accountDto.isOrg()) {
//            accountDto.setPhone(accountDto.getPhone().trim());
//            if (accountDto.getAccountId() == null || !objBackup.getPhone().equalsIgnoreCase(accountDto.getPhone().trim())) {
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
//            if(married) {
//                accountDto.setRelativeIdCardNumber(accountDto.getRelativeIdCardNumber().trim());
//                if (accountDto.getAccountId() == null || objBackup.getRelativeIdCardNumber() == null || !objBackup.getRelativeIdCardNumber().equalsIgnoreCase(accountDto.getRelativeIdCardNumber().trim())) {
//                    Account oldAccount = accountRepository.findByRelativeIdCardNumber(accountDto.getRelativeIdCardNumber());
//                    if (oldAccount != null) {
//                        setErrorForm("Số CMND/CCCD vợ/chồng đã tồn tại");
//                        FacesUtil.updateView("growl");
//                        return false;
//                    }
//                }
//            }
//        }
//        if(accountDto.isOrg()) {
//            accountDto.setPhone(accountDto.getPhone().trim());
//            if (accountDto.getAccountId() == null || !objBackup.getPhone().equalsIgnoreCase(accountDto.getPhone().trim())) {
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
//            if (accountDto.getAccountId() == null || !objBackup.getPhone().equalsIgnoreCase(accountDto.getPhone().trim())) {
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
        return true;
    }

    public void onSaveData() {
        showValidate = true;
        if (!validate()) {
            return;
        }
        accountDto.setProvinceId(cityDistrictCommuneController.getCityDistrictDto().getProvinceId());
        accountDto.setDistrictId(cityDistrictCommuneController.getCityDistrictDto().getDistrictId());
        accountDto.setCommuneId(cityDistrictCommuneController.getCityDistrictDto().getCommuneId());
        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);
        if(!married){
//            account.setRelativeIdCardNumber(null);
//            account.setRelativeName(null);
        }
        //send mail
        if (account.getAccountId() == null) {
            account.setRoleId(DbConstant.ROLE_ID_USER);
            account.setStatus(DbConstant.ACCOUNT_ACTIVE_STATUS);
            account.setSalt(StringUtil.generateSalt());
            String password = publicRegister ? account.getPassword() : StringUtil.generatePassword();
            account.setPassword(StringUtil.encryptPassword(password, account.getSalt()));
            account.setCreateBy(authorizationController.getAccountDto().getAccountId());
            account.setUpdateBy(authorizationController.getAccountDto().getAccountId());
            //account.setTimeToChangePassword(new Date());
            //send email
            EmailUtil.getInstance().sendPasswordUserEmail(ROLE_NAME_USER, account.getEmail(), password, account.getUserName(), account.getFullName());

        } else {
            account.setSalt(objBackup.getSalt());
            account.setPassword(objBackup.getPassword());
            account.setCreateBy(objBackup.getCreateBy());
            account.setCreateDate(objBackup.getCreateDate());
            account.setUpdateBy(authorizationController.getAccountDto().getAccountId());
        }
        accountRepository.save(account);

        FacesUtil.closeDialog("dialogInsertUpdate");
        resetAll();
        FacesUtil.updateView("growl");
        onSearch();
        FacesUtil.updateView("dlg");
    }

    public void showUpdatePopup(AccountDto obj) {
        married = false;
//        if (obj.isOrg()) {
//            tabindex = 1;
//        } else {
//            tabindex = 0;
//        }
//        if (StringUtils.isBlank(obj.getRelativeIdCardNumber())|| StringUtils.isBlank(obj.getRelativeName())) {
//            married = false;
//        }else {
//            married = true;
//        }

        objBackup = new AccountDto();
        BeanUtils.copyProperties(obj, accountDto);
        BeanUtils.copyProperties(accountDto, objBackup);
        cityDistrictCommuneController.resetAll();
        CityDistrictDto cityDistrictDto = new CityDistrictDto(obj.getProvinceId(), obj.getDistrictId(), obj.getCommuneId());
        cityDistrictCommuneController.setCityDistrictDto(cityDistrictDto);
        cityDistrictCommuneController.loadData();
    }

    public void resetDialog() {
        accountDto = new AccountDto();
        cityDistrictCommuneController.resetAll();
        married = false;
        tabindex = 0;
    }

    public void onDelete(AccountDto deleteObj) {
        try {
            accountRepository.deleteById(deleteObj.getAccountId());
            setSuccessForm("Xóa thành công");
            FacesUtil.updateView("growl");
            onSearch();
        } catch (Exception e) {
            setErrorForm("Xóa thất bại");
            FacesUtil.updateView("growl");
        }
    }

    public void onSearch() {
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
        lazyDataModel = new LazyDataModel<AccountDto>() {
            @Override
            public List<AccountDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                accountSearchDto.setPageIndex(first);
                accountSearchDto.setPageSize(pageSize);
                accountSearchDto.setSortField(sortField);
                String sort = "";
                if (sortOrder.equals(SortOrder.ASCENDING)) {
                    sort = "ASC";
                } else {
                    sort = "DESC";
                }
                accountSearchDto.setSortOrder(sort);
                return accountRepository.search(accountSearchDto);
            }

            @Override
            public AccountDto getRowData(String rowKey) {
                List<AccountDto> AccountList = getWrappedData();
                String value = String.valueOf(rowKey);
                for (AccountDto obj : AccountList) {
                    if (obj.getEmail().equals(value) || obj.getPhone().equals(value) ) {
                        return obj;
                    }
                }
                return null;
            }
        };
        int count = accountRepository.countSearch(accountSearchDto).intValue();
        lazyDataModel.setRowCount(count);
        FacesUtil.updateView("searchForm");
    }

    public void onTabChange() {
//        accountDto.setOrg(!accountDto.isOrg());
//        if (accountDto.isOrg()) {
//            tabindex = 1;
//        } else {
//            tabindex = 0;
//        }
    }

    public void reset() {
        accountDtos = new LazyDataModel<AccountDto>() {
            @Override
            public List<AccountDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                return new ArrayList<>();
            }
        };
        accountDtos.setRowCount(0);
        accountSearchDto.setKeyword(null);
    }

    public void onClickDetail(AccountDto accountDtoDetail) {
        accountDto = new AccountDto();
        accountSearchDto = new AccountSearchDto();
        BeanUtils.copyProperties(accountDtoDetail, accountDto);
        FacesUtil.redirect("/admin/quan-ly-nguoi-dung/chi-tiet-nguoi-dung.xhtml");
    }

    public void onUnlock(AccountDto ac) {
        AccountDto acc = new AccountDto();
        BeanUtils.copyProperties(ac,acc);
        ac.setStatus(DbConstant.ACCOUNT_ACTIVE_STATUS);
        //ac.setLoginFailed(0);
        //ac.setFirstTimeLogin(true);
        account = new Account();
        BeanUtils.copyProperties(ac, account);
        accountRepository.save(account);
        if(acc.getStatus().equals(DbConstant.ACCOUNT_LOCK_STATUS)) {
            setSuccessForm("Mở khóa tài khoản thành công");
        } else {
            setSuccessForm("Kích hoạt tài khoản thành công");
        }
        resetAll();
        onSearch();
    }

    public void onLock(AccountDto ac) {
        account = new Account();
        ac.setStatus(DbConstant.ACCOUNT_LOCK_STATUS);
        //ac.setLoginFailed(5);
        BeanUtils.copyProperties(ac, account);
        accountRepository.save(account);
        setSuccessForm("Khóa tài khoản thành công");
        resetAll();
        onSearch();
    }

    public String upperCaseFirstChar(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    @Override
    protected EScope getMenuId() {
        return EScope.ACCOUNT_USER;
    }
}


