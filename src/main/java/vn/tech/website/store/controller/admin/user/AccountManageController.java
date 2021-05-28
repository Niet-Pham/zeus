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
import vn.tech.website.store.controller.admin.common.UploadSingleImageController;
import vn.tech.website.store.dto.BrandDto;
import vn.tech.website.store.dto.common.CityDistrictDto;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.dto.user.AccountSearchDto;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.Account;
import vn.tech.website.store.model.Province;
import vn.tech.website.store.repository.AccountRepository;
import vn.tech.website.store.repository.ProvinceRepository;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.FacesUtil;
import vn.tech.website.store.util.StringUtil;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@Scope(value = "session")
@Getter
@Setter
public class AccountManageController extends BaseController {
    @Inject
    private AuthorizationController authorizationController;
    @Inject
    private CityDistrictCommuneController cityDistrictCommuneController;
    @Inject
    private UploadSingleImageController uploadSingleImageController;

    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    private ProvinceRepository provinceRepository;

    private LazyDataModel<AccountDto> lazyDataModel;
    private AccountDto accountDto;
    private AccountSearchDto searchDto;
    private List<SelectItem> listProvinceAccount;
    private List<Province> provinceList;
    private Integer typeRole;

    public void initDataCustomer() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            //init();
            typeRole = DbConstant.ROLE_ID_USER;
            resetAll(typeRole);
        }
    }

    public void initDataEmployee() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            //init();
            typeRole = DbConstant.ROLE_ID_EMPLOYEE;
            resetAll(typeRole);
        }
    }

    public void resetAll(Integer typeRole) {
        accountDto = new AccountDto();
        searchDto = new AccountSearchDto();
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
        onSearch(typeRole);
    }

    public void onSearch(Integer typeRole) {
        searchDto.setRoleId(typeRole);
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
        lazyDataModel = new LazyDataModel<AccountDto>() {
            @Override
            public List<AccountDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                searchDto.setPageIndex(first);
                searchDto.setPageSize(pageSize);
                searchDto.setSortField(sortField);
                String sort;
                if (sortOrder.equals(sortOrder.ASCENDING)) {
                    sort = "ASC";
                } else {
                    sort = "DESC";
                }
                searchDto.setSortOrder(sort);
                return accountRepository.search(searchDto);
            }

            @Override
            public AccountDto getRowData(String rowKey) {
                List<AccountDto> dtoList = getWrappedData();
                String value = String.valueOf(rowKey);
                for (AccountDto obj : dtoList) {
                    if (obj.getAccountId().equals(Long.valueOf(value))) {
                        return obj;
                    }
                }
                return null;
            }
        };

        int count = accountRepository.countSearch(searchDto).intValue();
        lazyDataModel.setRowCount(count);
    }

    public boolean validateData() {
        if (StringUtils.isBlank(accountDto.getUserName())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập tài khoản");
            return false;
        } else {
            accountDto.setUserName(removeSpaceOfString(accountDto.getUserName()));
        }
        if (StringUtils.isBlank(accountDto.getPassword()) && accountDto.getAccountId() != null) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập mật khẩu");
            return false;
        }
        List<Account> accountList = new ArrayList<>();
        if (accountDto.getAccountId() == null) {
            accountList = accountRepository.findAll();
        } else {
            accountList = accountRepository.findAllAccountExpertId(accountDto.getAccountId());
        }
        for (Account account : accountList) {
            if (accountDto.getUserName().equalsIgnoreCase(removeSpaceOfString(account.getUserName()))) {
                FacesUtil.addErrorMessage("Tên tài khoản đã tồn tại");
                return false;
            }
        }
        if (StringUtils.isBlank(accountDto.getFullName())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập họ tên");
            return false;
        } else {
            accountDto.setFullName(removeSpaceOfString(accountDto.getFullName()));
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
        if (StringUtils.isBlank(accountDto.getEmail())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập email");
            return false;
        }
        if (!accountDto.getEmail().matches("^\\s*[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\\s*$")) {
            FacesUtil.addErrorMessage("Email không đúng định dạng");
            return false;
        }
        //validate + check phone,email
        if (accountDto.getAccountId() == null) {
            accountList = accountRepository.findAccountByRoleId(typeRole);
        } else {
            accountList = accountRepository.findAllAccountByRoleIdExpertId(typeRole, accountDto.getAccountId());
        }
        for (Account account : accountList) {
            if (accountDto.getPhone().equalsIgnoreCase(removeSpaceOfString(account.getPhone()))) {
                FacesUtil.addErrorMessage("Số điện thoại đã tồn tại");
                return false;
            }
            if (accountDto.getEmail().equalsIgnoreCase(removeSpaceOfString(account.getEmail()))) {
                FacesUtil.addErrorMessage("Email đã tồn tại");
                return false;
            }
        }
        if (StringUtils.isBlank(accountDto.getAddress())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập địa chỉ");
            return false;
        } else {
            accountDto.setAddress(removeSpaceOfString(accountDto.getAddress()));
        }
        return true;
    }

    public void onSave() {
        if (!validateData()) {
            return;
        }
        Account account = new Account();
        if (accountDto.getAccountId() == null) {
            accountDto.setSalt(StringUtil.generateSalt());
            accountDto.setPassword(StringUtil.encryptPassword(Constant.ACCOUNT_PASSWORD_DEFAULT, accountDto.getSalt()));
        }else {
            Account accCheckPass = accountRepository.findAccountByAccountId(accountDto.getAccountId());
            if (!accountDto.getPassword().equals(accCheckPass.getPassword())){
                accountDto.setSalt(StringUtil.generateSalt());
                accountDto.setPassword(StringUtil.encryptPassword(accountDto.getPassword(), accountDto.getSalt()));
            }
        }
        accountDto.setImagePath(uploadSingleImageController.getImagePath());
        accountDto.setProvinceId(cityDistrictCommuneController.getCityDistrictDto().getProvinceId());
        accountDto.setDistrictId(cityDistrictCommuneController.getCityDistrictDto().getDistrictId());
        accountDto.setCommuneId(cityDistrictCommuneController.getCityDistrictDto().getCommuneId());
        BeanUtils.copyProperties(accountDto, account);
        account.setStatus(DbConstant.ACCOUNT_ACTIVE_STATUS);
        account.setRoleId(typeRole);
        account.setUpdateDate(new Date());
        account.setUpdateBy(authorizationController.getAccountDto() == null ? authorizationController.getAccountDto().getAccountId() : 1);
        accountRepository.save(account);
        FacesUtil.addSuccessMessage("Lưu thành công.");
        FacesUtil.closeDialog("dialogInsertUpdate");
        onSearch(typeRole);
    }

    public void showUpdatePopup(AccountDto resultDto) {
        BeanUtils.copyProperties(resultDto, accountDto);
        uploadSingleImageController.resetAll(resultDto.getImagePath());
        CityDistrictDto cityDistrictDto = new CityDistrictDto(accountDto.getProvinceId(), accountDto.getDistrictId(), accountDto.getCommuneId());
        cityDistrictCommuneController.setCityDistrictDto(cityDistrictDto);
        cityDistrictCommuneController.loadData();
    }

    public void onDelete(AccountDto resultDto) {
        resultDto.setStatus(DbConstant.ACCOUNT_INACTIVE_STATUS);
        Account account = new Account();
        BeanUtils.copyProperties(resultDto, account);
        accountRepository.save(account);
        FacesUtil.addSuccessMessage("Xóa thành công.");
        onSearch(typeRole);
    }

    public void resetDialog() {
        accountDto = new AccountDto();

    }

    public void resetPassword(AccountDto resultDto){
        BeanUtils.copyProperties(resultDto, accountDto);
        accountDto.setSalt(StringUtil.generateSalt());
        accountDto.setPassword(StringUtil.encryptPassword(Constant.ACCOUNT_PASSWORD_DEFAULT, accountDto.getSalt()));
        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);
        accountRepository.save(account);
        FacesUtil.addSuccessMessage("Reset mật khẩu thành công.");
        FacesUtil.closeDialog("dialogInsertUpdate");
        onSearch(typeRole);
    }

    public String removeSpaceOfString(String str) {
        return str.trim().replaceAll("[\\s|\\u00A0]+", " ");
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }
}
