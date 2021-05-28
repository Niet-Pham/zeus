package vn.tech.website.store.controller.admin.base;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.admin.BaseController;
import vn.tech.website.store.controller.admin.auth.AuthorizationController;
import vn.tech.website.store.dto.base.ProvinceSearchDto;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.Account;
import vn.tech.website.store.model.Commune;
import vn.tech.website.store.model.District;
import vn.tech.website.store.model.Province;
import vn.tech.website.store.repository.AccountRepository;
import vn.tech.website.store.repository.CommuneRepository;
import vn.tech.website.store.repository.DistrictRepository;
import vn.tech.website.store.repository.ProvinceRepository;
import vn.tech.website.store.service.CommuneService;
import vn.tech.website.store.service.DistrictService;
import vn.tech.website.store.util.FacesUtil;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@Getter
@Setter
@Scope(value = "session")
public class ProvinceController extends BaseController {
    @Inject
    private AuthorizationController authorizationController;

    @Autowired
    protected DistrictRepository districtRepository;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private CommuneService communeService;
    @Autowired
    protected CommuneRepository communeRepository;
    @Autowired
    protected ProvinceRepository provinceRepository;

    private Province province;
    private LazyDataModel<Province> lazyDataModel;
    private ProvinceSearchDto provinceSearchDto;
    private List<SelectItem> provinceList;
    private Province provinceCopy;
    private Province provinceCheck;
    private String labelSynchronized;
    private String labelVersion;
    private List<Account> account;
    private List<Account> accountCheck;
    private boolean showSuccess;
    private boolean checkNoteLabel = true;
    private Long idVersion;
    private Long versionUse = 0L;
    private boolean showSuccessVersion;
    private List<Account> accountList = new ArrayList<>();
    private List<Province> provinceListAdd = new ArrayList<>();
    private List<District> districtListAdd = new ArrayList<>();
    private List<Commune> communeListAdd = new ArrayList<>();
    private Account account1;
    private Long provinceId;
    private Long provinceIdOfIssue;


    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAll();
        }
    }

    public void resetAll() {
        account1 = new Account();
        provinceList = new ArrayList<>();
        province = new Province();
        provinceSearchDto = new ProvinceSearchDto();
        onSearch();
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");

        if (showSuccess) {
            FacesUtil.addSuccessMessage("Đồng bộ thành công");
            onSearch();
            showSuccess = false;
        }
    }


    public void onSaveData() {
//        if (province.getProvinceId() != null) {
//            if (accountRepository.existsAccountsByProvinceIdOrProvinceIdOfIssue(province.getProvinceId(), province.getProvinceId())) {
//                FacesUtil.addErrorMessage("Không được sửa vì " + provinceCopy.getName() + " đang được sử dụng");
//                FacesUtil.updateView("growl");
//                FacesUtil.closeDialog("dialogInsertUpdate");
//                return;
//            }
//        }
        List<Province> pro = (List<Province>) provinceRepository.findAll();

        province.setName(province.getName().trim());
        if (province.getProvinceId() == null || !province.getName().trim().equalsIgnoreCase(provinceCopy.getName().trim())) {
            List<Province> oldCode = provinceRepository.findByName(province.getName());
            if (oldCode.size() != 0) {
                FacesUtil.addErrorMessage("Tên tỉnh/thành phố đã tồn tại");
                FacesUtil.updateView("growl");
                return;
            }
        }

        FacesUtil.addSuccessMessage("Lưu thành công");
        FacesUtil.closeDialog("dialogInsertUpdate");
        resetAll();
        FacesUtil.updateView("growl");
        FacesUtil.updateView("searchForm");
        onSearch();
    }

    public void clearData() {
        if (province.getProvinceId() != null && province.getProvinceId() > 0) {
            BeanUtils.copyProperties(provinceCopy, province);
        } else
            resetDialog();
    }

    public void showUpdatePopup(Province obj) {
        provinceCopy = new Province();
        BeanUtils.copyProperties(obj, province);
        BeanUtils.copyProperties(obj, provinceCopy);
    }


    public void resetDialog() {
        province = new Province();
    }

    public void onDelete(Province deleteObj) {
        try {
            account = new ArrayList<>();
//            account = accountRepository.findByProvinceIdOfIssue(deleteObj.getProvinceId());
            List<Account> accounts = accountRepository.findAccountsByProvinceId(deleteObj.getProvinceId());
            if (account.size() == 0 && accounts.size() == 0) {
                communeService.deleteCommune(deleteObj.getProvinceId());
                districtService.deleteDistrict(deleteObj.getProvinceId());
                FacesUtil.addSuccessMessage("Xóa thành công");
                onSearch();
            } else {
                FacesUtil.addErrorMessage("Xóa thất bại vì " + deleteObj.getName() + " đã được sử dụng để tạo tài khoản");
            }

        } catch (Exception e) {
            FacesUtil.addErrorMessage("Xóa thất bại");
        }
    }

    public void onSearch() {
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
        lazyDataModel = new LazyDataModel<Province>() {

            @Override
            public List<Province> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                provinceSearchDto.setPageIndex(first);
                provinceSearchDto.setPageSize(pageSize);
                provinceSearchDto.setSortField(sortField);
                String sort = "";
                if (sortOrder.equals(SortOrder.ASCENDING)) {
                    sort = "ASC";
                } else {
                    sort = "DESC";
                }
                provinceSearchDto.setSortOrder(sort);
                return provinceRepository.search(provinceSearchDto);
            }

            @Override
            public Province getRowData(String rowKey) {
                List<Province> provinceList = getWrappedData();
                String value = String.valueOf(rowKey);
                for (Province obj : provinceList) {
                    if (obj.getProvinceId().equals(value) || obj.getName().equals(value)) {
                        return obj;
                    }
                }
                return null;
            }
        };
        int count = provinceRepository.countSearch(provinceSearchDto).intValue();
        lazyDataModel.setRowCount(count);
        FacesUtil.updateView("searchForm");
    }

    public boolean checkEditButton(Long provinceId, Long provinceIdOfIssue) {
        List<Long> listProviceId = accountRepository.findProvinceId();
        //List<Long> listProvinceIdOfIssue = accountRepository.findProvinceIdOfIssue();
        if (listProviceId.contains(provinceId)) {
            return true;
        }
//        if (listProvinceIdOfIssue.contains(provinceIdOfIssue)) {
//            return true;
//        }
        return false;
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }
}

