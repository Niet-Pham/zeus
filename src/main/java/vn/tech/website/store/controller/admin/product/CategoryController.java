package vn.tech.website.store.controller.admin.product;

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
import vn.tech.website.store.dto.CategoryDto;
import vn.tech.website.store.dto.CategorySearchDto;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.Category;
import vn.tech.website.store.repository.CategoryRepository;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.FacesUtil;

import javax.faces.context.FacesContext;
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
public class CategoryController extends BaseController {
    @Inject
    private AuthorizationController authorizationController;

    @Autowired
    private CategoryRepository categoryRepository;

    private LazyDataModel<CategoryDto> lazyDataModel;
    private CategoryDto categoryDto;
    private CategorySearchDto searchDto;

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAll();
        }
    }

    public void resetAll() {
        categoryDto = new CategoryDto();
        categoryDto.setType(DbConstant.CATEGORY_TYPE_PRODUCT);

        searchDto = new CategorySearchDto();
        searchDto.setType(DbConstant.CATEGORY_TYPE_PRODUCT);
        onSearch();
    }

    public void onSearch() {
        searchDto.setType(DbConstant.CATEGORY_TYPE_PRODUCT);
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
        lazyDataModel = new LazyDataModel<CategoryDto>() {
            @Override
            public List<CategoryDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
                return categoryRepository.search(searchDto);
            }

            @Override
            public CategoryDto getRowData(String rowKey) {
                List<CategoryDto> dtoList = getWrappedData();
                String value = String.valueOf(rowKey);
                for (CategoryDto obj : dtoList) {
                    if (obj.getCategoryId().equals(Long.valueOf(value)) || obj.getCategoryName().equals(value)) {
                        return obj;
                    }
                }
                return null;
            }
        };

        int count = categoryRepository.countSearch(searchDto).intValue();
        lazyDataModel.setRowCount(count);
    }

    public boolean validateData() {
        if (StringUtils.isBlank(categoryDto.getCode())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập mã loại sản phẩm");
            return false;
        }
        if (StringUtils.isBlank(categoryDto.getCategoryName())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập tên loại sản phẩm");
            return false;
        }
        List<Category> categoryList = new ArrayList<>();
        if (categoryDto.getCategoryId() == null) {
            categoryList = categoryRepository.findAllCategoryProduct();
        } else {
            categoryList = categoryRepository.findAllCategoryProductExpertId(categoryDto.getCategoryId());
        }
        categoryDto.setCategoryName(removeSpaceOfString(categoryDto.getCategoryName()));
        categoryDto.setCode(removeSpaceOfString(categoryDto.getCode()));
        for (Category category : categoryList) {
            if (categoryDto.getCode().equalsIgnoreCase(removeSpaceOfString(category.getCode()))) {
                FacesUtil.addErrorMessage("Mã loại sản phẩm này đã tồn tại");
                return false;
            }
            if (categoryDto.getCategoryName().equalsIgnoreCase(removeSpaceOfString(category.getCategoryName()))) {
                FacesUtil.addErrorMessage("Tên loại sản phẩm này đã tồn tại");
                return false;
            }
        }
        return true;
    }

    public void onSave() {
        if (!validateData()) {
            return;
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        category.setType(DbConstant.CATEGORY_TYPE_PRODUCT);
        category.setUpdateDate(new Date());
        category.setUpdateBy(authorizationController.getAccountDto() == null ? authorizationController.getAccountDto().getAccountId() : 1);
        categoryRepository.save(category);
        FacesUtil.addSuccessMessage("Lưu thành công.");
        FacesUtil.closeDialog("dialogInsertUpdate");
        onSearch();
    }

    public void showUpdatePopup(CategoryDto resultDto) {
        BeanUtils.copyProperties(resultDto, categoryDto);
    }

    public void onDelete(CategoryDto resultDto) {
        resultDto.setStatus(DbConstant.CATEGORY_STATUS_INACTIVE);
        Category category = new Category();
        BeanUtils.copyProperties(resultDto, category);
        categoryRepository.save(category);
        FacesUtil.addSuccessMessage("Xóa thành công.");
        onSearch();
    }

    public void resetDialog() {
        categoryDto = new CategoryDto();
        categoryDto.setType(DbConstant.CATEGORY_TYPE_PRODUCT);
    }

    public String removeSpaceOfString(String str) {
        return str.trim().replaceAll("[\\s|\\u00A0]+", " ");
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }
}
