package vn.tech.website.store.controller.admin.orders;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.admin.BaseController;
import vn.tech.website.store.controller.admin.common.UploadSingleImageController;
import vn.tech.website.store.dto.ShippingDto;
import vn.tech.website.store.dto.ShippingSearchDto;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.Shipping;
import vn.tech.website.store.repository.ShippingRepository;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.FacesUtil;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
@Scope(value = "session")
@Getter
@Setter
public class ShippingController extends BaseController {
    @Inject
    private UploadSingleImageController uploadSingleImageController;

    @Autowired
    private ShippingRepository shippingRepository;

    private LazyDataModel<ShippingDto> lazyDataModel;
    private ShippingDto shippingDto;
    private ShippingSearchDto searchDto;

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAll();
        }
    }

    public void resetAll() {
        shippingDto = new ShippingDto();
        searchDto = new ShippingSearchDto();
        uploadSingleImageController.resetAll(null);
        onSearch();
    }

    public void onSearch() {
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
        lazyDataModel = new LazyDataModel<ShippingDto>() {
            @Override
            public List<ShippingDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
                return shippingRepository.search(searchDto);
            }

            @Override
            public ShippingDto getRowData(String rowKey) {
                List<ShippingDto> dtoList = getWrappedData();
                String value = String.valueOf(rowKey);
                for (ShippingDto obj : dtoList) {
                    if (obj.getShippingId().equals(Long.valueOf(value))) {
                        return obj;
                    }
                }
                return null;
            }
        };

        int count = shippingRepository.countSearch(searchDto).intValue();
        lazyDataModel.setRowCount(count);
    }

    public boolean validateData() {
        if (StringUtils.isBlank(shippingDto.getName())){
            FacesUtil.addErrorMessage("Bạn vui lòng nhập tên đơn vị vận chuyển");
            return false;
        }
        if (StringUtils.isBlank(shippingDto.getCode())){
            FacesUtil.addErrorMessage("Bạn vui lòng nhập mã đơn vị vận chuyển");
            return false;
        }
        if (shippingDto.getPrice() == null){
            FacesUtil.addErrorMessage("Bạn vui lòng nhập giá của đơn vị vận chuyển");
            return false;
        }
        if (StringUtils.isBlank(shippingDto.getDetail())){
            FacesUtil.addErrorMessage("Bạn vui lòng nhập mô tả chi tiết");
            return false;
        }
        shippingDto.setPath(uploadSingleImageController.getImagePath());
        if (shippingDto.getPath() == null){
            FacesUtil.addErrorMessage("Bạn vui lòng tải ảnh cho đơn vị vận chuyển");
            return false;
        }
        return true;
    }

    public void onSave() {
        if (!validateData()) {
            return;
        }
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingDto, shipping);
        shipping.setPath(uploadSingleImageController.getImagePath());
        shipping.setStatus(DbConstant.SHIPPING_STATUS_ACTIVE);
        shippingRepository.save(shipping);
        FacesUtil.addSuccessMessage("Lưu thành công.");
        FacesUtil.closeDialog("dialogInsertUpdate");
        onSearch();
    }

    public void showUpdatePopup(ShippingDto resultDto) {
        uploadSingleImageController.resetAll(resultDto.getPath());
        BeanUtils.copyProperties(resultDto, shippingDto);
    }

    public void onDelete(ShippingDto resultDto) {
        resultDto.setStatus(DbConstant.SHIPPING_STATUS_INACTIVE);
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(resultDto, shipping);
        shippingRepository.save(shipping);
        FacesUtil.addSuccessMessage("Xóa thành công.");
        onSearch();
    }

    public void resetDialog() {
        shippingDto = new ShippingDto();
        uploadSingleImageController.resetAll(null);
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }
}
