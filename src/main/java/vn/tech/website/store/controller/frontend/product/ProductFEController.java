package vn.tech.website.store.controller.frontend.product;


import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.frontend.BaseFEController;
import vn.tech.website.store.controller.frontend.common.PaginationController;
import vn.tech.website.store.dto.ProductDto;
import vn.tech.website.store.dto.ProductOptionDto;
import vn.tech.website.store.dto.ProductOptionSearchDto;
import vn.tech.website.store.dto.ProductSearchDto;
import vn.tech.website.store.model.Brand;
import vn.tech.website.store.model.Category;
import vn.tech.website.store.model.Product;
import vn.tech.website.store.model.ProductLink;
import vn.tech.website.store.repository.*;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.FacesUtil;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@Scope(value = "session")
@Getter
@Setter
public class ProductFEController extends BaseFEController {
    @Inject
    private ProductDetailFEController productDetailFEController;
    @Inject
    private HttpServletRequest request;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductLinkRepository productLinkRepository;
    @Autowired
    private ProductOptionRepository productOptionRepository;

    private Long categoryId;
    private Long brandId;
    private Long productIdParams;
    private String cateCode;
    private ProductSearchDto searchDto;
    private ProductSearchDto productSearchDto;
    private List<ProductOptionDto> productOptionDtoList;
    private List<SelectItem> categories;
    private List<SelectItem> brands;
    private List<SelectItem> colorOptions;
    private List<SelectItem> sizeOptions;
    private List<SelectItem> releaseOptions;
    private List<SelectItem> otherOptions;
    private PaginationController<ProductDto> pagination;
    private boolean checkType;

    public ProductFEController() {
        pagination = new PaginationController<>();
    }

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            String cateId = request.getParameter("catid");
            categoryId = cateId == null ? null : Long.parseLong(cateId);
            String braId = request.getParameter("braid");
            brandId = braId == null ? null : Long.parseLong(braId);
            String proId = request.getParameter("proid");
            productIdParams = proId == null ? null : Long.parseLong(proId);
            String categoryCode = request.getParameter("catecode");
            cateCode = categoryCode == null ? null : categoryCode;
            resetAll();
        }
    }

    public void resetAll() {
        productSearchDto = new ProductSearchDto();
        productSearchDto.setMinPrice(0L);
        productSearchDto.setMaxPrice(0L);
        productSearchDto.setEnableSearchPrice(false);
        checkType = true;

        Product productFromNoti = productRepository.getByProductId(productIdParams);
        if (productFromNoti != null){
            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(productFromNoti,productDto);
            viewDetailProduct(productDto);
            return;
        }

        pagination.setRequest(request);
        searchDto = new ProductSearchDto();
        searchDto.setCategoryId(categoryId);
        if (cateCode != null) {
            searchDto.setCategoryId(categoryRepository.getCateIdByCode(cateCode).getCategoryId());
        }
        searchDto.setBrandId(brandId);
        onSearch(searchDto);

        categories = new ArrayList<>();
        List<Category> categoryList = categoryRepository.findAllCategoryProduct();
        for (Category c : categoryList) {
            categories.add(new SelectItem(c.getCategoryId(), c.getCategoryName()));
        }
        brands = new ArrayList<>();
        List<Brand> brandList = brandRepository.findAll();
        for (Brand b : brandList) {
            brands.add(new SelectItem(b.getBrandId(), b.getBrandName()));
        }

        colorOptions = new ArrayList<>();
        sizeOptions = new ArrayList<>();
        releaseOptions = new ArrayList<>();
        otherOptions = new ArrayList<>();
        productOptionDtoList = productOptionRepository.search(new ProductOptionSearchDto());
        for (ProductOptionDto option : productOptionDtoList) {
            switch (option.getType()) {
                case DbConstant.OPTION_TYPE_COLOR:
                    colorOptions.add(new SelectItem(option.getProductOptionId(), option.getOptionName()));
                    break;
                case DbConstant.OPTION_TYPE_SIZE:
                    sizeOptions.add(new SelectItem(option.getProductOptionId(), option.getOptionName()));
                    break;
                case DbConstant.OPTION_TYPE_OTHER:
                    otherOptions.add(new SelectItem(option.getProductOptionId(), option.getOptionName()));
                    break;
                default:
                    break;
            }
        }
    }

    public void onSearch(ProductSearchDto searchDto) {
        searchDto.setExpertType(DbConstant.PRODUCT_TYPE_PARENT);
        pagination.setLazyDataModel(new LazyDataModel<ProductDto>() {
            @Override
            public List<ProductDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                searchDto.setPageIndex(first);
                searchDto.setPageSize(pageSize);
                searchDto.setSortField(sortField);
                String sort = "";
                if (sortOrder == null || sortOrder.equals(SortOrder.DESCENDING)) {
                    sort = "DESC";
                } else {
                    sort = "ASC";
                }
                searchDto.setSortOrder(sort);
                return productRepository.search(searchDto);
            }

            @Override
            public int getRowCount() {
                return productRepository.countSearch(searchDto).intValue();
            }
        });
        pagination.loadData();
    }

    public void viewDetailProduct(ProductDto productDto) {
        ProductSearchDto searchDto = new ProductSearchDto();
        if (productDto.getType() == DbConstant.PRODUCT_TYPE_PARENT) {
            productDetailFEController.setProductId(null);

            productDetailFEController.setParentId(productDto.getProductId());
            searchDto.setParentId(productDto.getProductId());
        } if (productDto.getType() == DbConstant.PRODUCT_TYPE_CHILD){
            productDetailFEController.setProductId(productDto.getProductId());

            ProductLink productLink = productLinkRepository.findProductLinkByChildId(productDto.getProductId());
            Long parentId = productLink.getParentId();
            productDetailFEController.setParentId(parentId);
            searchDto.setParentId(parentId);
        }
        if (productDto.getType() == DbConstant.PRODUCT_TYPE_NONE){
            productDetailFEController.setParentId(null);
            productDetailFEController.setProductId(productDto.getProductId());
            Map<Long, ProductDto> childProductMap = new HashMap<>();
            childProductMap.put(productDto.getProductId(), productDto);
            productDetailFEController.setChildProductMap(new HashMap<>());
            productDetailFEController.setChildProductMap(childProductMap);

            FacesUtil.redirect("/frontend/product/detail.xhtml");
            return;
        }

        List<ProductDto> childProducts = productRepository.search(searchDto);
        Map<Long, ProductDto> childProductMap = new HashMap<>();
        for (ProductDto child : childProducts) {
            childProductMap.put(child.getProductId(), child);
        }
        productDetailFEController.setChildProductMap(new HashMap<>());
        productDetailFEController.setChildProductMap(childProductMap);

        FacesUtil.redirect("/frontend/product/detail.xhtml");
    }

    public void resetDialog() {
        resetAll();
        FacesUtil.updateView("searchForm");
        FacesUtil.updateView("productList");
    }

    public void onSearchDialog() {
        if (productSearchDto.getMaxPrice() < productSearchDto.getMinPrice()) {
            setErrorForm("Giá tối đa không được nhỏ hơn giá tối thiểu.");
            FacesUtil.updateView(Constant.ERROR_FE_GROWL_ID);
            return;
        }

//        productSearchDto.setCategoryId(categoryId);
//        productSearchDto.setBrandId(brandId);
        if (productSearchDto.getOptionColorId() == -1 && productSearchDto.getOptionSizeId() == -1 && productSearchDto.getOptionOtherId() == -1) {
            productSearchDto.setOptionCondition(null);
        } else {
            String condition = "("
                    + (productSearchDto.getOptionColorId() != -1 ? (productSearchDto.getOptionColorId()) : "")
                    + (productSearchDto.getOptionSizeId() != -1 ? (", " + productSearchDto.getOptionSizeId()) : "")
                    + (productSearchDto.getOptionOtherId() != -1 ? (", " + productSearchDto.getOptionOtherId()) : "")
                    + ")";
            productSearchDto.setOptionCondition(condition);
        }

        onSearch(productSearchDto);
        FacesUtil.updateView("productList");
    }

    @Override
    protected String getMenuId() {
        return null;
    }
}
