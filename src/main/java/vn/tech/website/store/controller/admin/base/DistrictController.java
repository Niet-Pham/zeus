package vn.tech.website.store.controller.admin.base;

import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.admin.BaseController;
import vn.tech.website.store.controller.admin.CommonController;
import vn.tech.website.store.controller.admin.auth.AuthorizationController;
import vn.tech.website.store.dto.base.DistrictDto;
import vn.tech.website.store.dto.base.DistrictSearchDto;
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
@Scope(value = "session")
@Getter
@Setter
public class DistrictController extends BaseController {

    @Inject
    private AuthorizationController authorizationController;
    @Inject
    private CommonController commonController;

    @Autowired
    protected CommuneRepository communeRepository;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private CommuneService communeService;
    @Autowired
    protected DistrictRepository districtRepository;
    @Autowired
    protected ProvinceRepository provinceRepository;
    @Autowired
    protected AccountRepository accountRepository;

    private District district;
    private LazyDataModel<DistrictDto> lazyDataModel;
    private String labelVersion;
    private DistrictSearchDto districtSearchDto;
    private DistrictDto districtDto;
    private List<SelectItem> districtList;
    private District districtCopy;
    private List<SelectItem> provinceList;
    private String labelSynchronized;
    private boolean showSuccess;
    private boolean showSuccessVersion;
    private Long idVersion;
    private Long versionUse = 0L;
    private List<Account> accountList= new ArrayList<>();
    private List<Province> provinceListAdd = new ArrayList<>();
    private List<District> districtListAdd = new ArrayList<>();
    private List<Commune> communeListAdd = new ArrayList<>();

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            versionUse = 0L;
            resetAll();
        }
    }

    public void resetAll() {
        districtList = new ArrayList<>();
        district = new District();
        districtDto = new DistrictDto();
        districtSearchDto = new DistrictSearchDto();
        provinceList = new ArrayList<>();
        List<Province> province = (List<Province>) provinceRepository.findAll();
        for (Province prv : province) {
            provinceList.add(new SelectItem(prv.getProvinceId(), commonController.upperCaseFirstChar(prv.getName())));
        }
        onSearch();
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
        if (showSuccess) {
            FacesUtil.addSuccessMessage("Đồng bộ thành công");
            onSearch();
            showSuccess = false;
        }
        versionUse = 0L;
        if (showSuccessVersion) {
            FacesUtil.addSuccessMessage("Thay đổi version thành công");
            onSearch();
            showSuccessVersion = false;
        }
    }

    public void onSaveData() {

        List<Account> checkDistrict = accountRepository.findAccountsByDistrictId(district.getDistrictId());
        if ((checkDistrict.size() != 0 && district.getDistrictId() != null)) {
            FacesUtil.addErrorMessage("Không được sửa vì " + districtCopy.getName() + " đang được sử dụng");
            FacesUtil.updateView("growl");
            FacesUtil.closeDialog("dialogInsertUpdate");
            return;
        }


        if (district.getDistrictId() == null ) {

                FacesUtil.addErrorMessage("Mã quận/huyện đã tồn tại");
                FacesUtil.updateView("growl");
                return;

        }

        district.setName(district.getName().trim());
        if (district.getDistrictId() == null ) {

                FacesUtil.addErrorMessage("Tên quận/huyện đã tồn tại trong tỉnh bạn chọn");
                FacesUtil.updateView("growl");
                return;
        }
        FacesUtil.addSuccessMessage("Lưu thành công");
        FacesUtil.closeDialog("dialogInsertUpdate");
        resetAll();
        FacesUtil.updateView("growl");
        FacesUtil.updateView("searchForm");
        onSearch();
    }

    public void showUpdatePopup(District obj) {
        districtCopy = new DistrictDto();
        BeanUtils.copyProperties(obj, district);
        BeanUtils.copyProperties(obj, districtCopy);
    }


    public void resetDialog() {
        district = new District();
    }

    public void onDelete(District deleteObj) {
        try {
            List<Account> checkDistrict = accountRepository.findAccountsByDistrictId(deleteObj.getDistrictId());
            if (checkDistrict.size() == 0) {
                districtService.deleteDistrictByDistrictId(deleteObj.getDistrictId());
                communeService.deleteCommunesByDistrictId(deleteObj.getDistrictId());
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
        lazyDataModel = new LazyDataModel<DistrictDto>() {

            @Override
            public List<DistrictDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                districtSearchDto.setPageIndex(first);
                districtSearchDto.setPageSize(pageSize);
                districtSearchDto.setSortField(sortField);
                String sort;
                if (sortOrder.equals(SortOrder.ASCENDING)) {
                    sort = "ASC";
                } else {
                    sort = "DESC";
                }
                districtSearchDto.setSortOrder(sort);
                return districtRepository.search(districtSearchDto);
            }

            @Override
            public DistrictDto getRowData(String rowKey) {
                List<DistrictDto> districtList = getWrappedData();
                String value = String.valueOf(rowKey);
                for (DistrictDto obj : districtList) {
                    if ( obj.getName().equals(value)) {
                        return obj;
                    }

                }
                return null;
            }
        };
        int count = districtRepository.countSearch(districtSearchDto).intValue();
        lazyDataModel.setRowCount(count);
        FacesUtil.updateView(" ");
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }


}

