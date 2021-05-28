package vn.tech.website.store.controller.admin.product;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.docProps.variantTypes.Null;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.admin.BaseController;
import vn.tech.website.store.controller.admin.auth.AuthorizationController;
import vn.tech.website.store.controller.admin.common.UploadMultipleImageController;
import vn.tech.website.store.dto.ProductDto;
import vn.tech.website.store.dto.ProductSearchDto;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.*;
import vn.tech.website.store.repository.*;
import vn.tech.website.store.util.*;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
@Scope(value = "session")
@Getter
@Setter
public class ProductController extends BaseController {
    @Inject
    private AuthorizationController authorizationController;
    @Inject
    private UploadMultipleImageController uploadMultipleImageController;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductOptionRepository productOptionRepository;
    @Autowired
    private ProductLinkRepository productLinkRepository;
    @Autowired
    private ProductOptionDetailRepository productOptionDetailRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductHighLightRepository productHighLightRepository;
    @Autowired
    private SendNotificationRepository sendNotificationRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    private LazyDataModel<ProductDto> lazyDataModel;
    private ProductDto productDto;
    private ProductSearchDto searchDto;
    private List<SelectItem> categoryList;
    private List<SelectItem> brandList;
    private List<SelectItem> typeList;
    private List<SelectItem> productParentList;
    private List<SelectItem> optionList;
    private List<Long> listOptionSelect;


    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAll();
        }
    }

    public void resetAll() {
        productDto = new ProductDto();
        searchDto = new ProductSearchDto();

        // include upload image controller
        uploadMultipleImageController.resetAll(null);

        //add combobox category
        categoryList = new ArrayList<>();
        List<Category> categories = categoryRepository.findAllCategoryProduct();
        for (Category obj : categories) {
            categoryList.add(new SelectItem(obj.getCategoryId(), obj.getCategoryName()));
        }
        //add combobox brand
        brandList = new ArrayList<>();
        List<Brand> brands = brandRepository.findAll();
        for (Brand obj : brands) {
            brandList.add(new SelectItem(obj.getBrandId(), obj.getBrandName()));
        }
        //add combobox type
        typeList = new ArrayList<>();
        typeList.add(new SelectItem(DbConstant.PRODUCT_TYPE_PARENT, DbConstant.PRODUCT_TYPE_PARENT_STRING));
        typeList.add(new SelectItem(DbConstant.PRODUCT_TYPE_CHILD, DbConstant.PRODUCT_TYPE_CHILD_STRING));
        typeList.add(new SelectItem(DbConstant.PRODUCT_TYPE_NONE, DbConstant.PRODUCT_TYPE_NONE_STRING));
        listOptionSelect = new ArrayList<>();
        //add combobox productParent
        productParentList = new ArrayList<>();
        List<Product> parentList = productRepository.getAllByType(DbConstant.PRODUCT_TYPE_PARENT);
        for (Product obj : parentList) {
            productParentList.add(new SelectItem(obj.getProductId(), obj.getProductName()));
        }
        //add combobox option
        optionList = new ArrayList<>();
        List<ProductOption> options = productOptionRepository.findAll();
        for (ProductOption obj : options) {
            optionList.add(new SelectItem(obj.getProductOptionId(), obj.getOptionName() + "(" + obj.getOptionValue() + ")"));
        }
        onSearch();
    }

    public void onSearch() {
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
        lazyDataModel = new LazyDataModel<ProductDto>() {
            @Override
            public List<ProductDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
                return productRepository.search(searchDto);
            }

            @Override
            public ProductDto getRowData(String rowKey) {
                List<ProductDto> dtoList = getWrappedData();
                String value = String.valueOf(rowKey);
                for (ProductDto obj : dtoList) {
                    if (obj.getProductId().equals(Long.valueOf(value))) {
                        return obj;
                    }
                }
                return null;
            }
        };

        int count = productRepository.countSearch(searchDto).intValue();
        lazyDataModel.setRowCount(count);
    }

    public void resetDialog() {
        productDto = new ProductDto();
        uploadMultipleImageController.resetAll(null);
        listOptionSelect = new ArrayList<>();
    }

    public boolean validateData() {
        if (productDto.getCategoryId() == null) {
            FacesUtil.addErrorMessage("Bạn vui lòng chọn loại sản phẩm");
            return false;
        }
        if (productDto.getBrandId() == null) {
            FacesUtil.addErrorMessage("Bạn vui lòng chọn thương hiệu");
            return false;
        }
        if (productDto.getType() == null) {
            FacesUtil.addErrorMessage("Bạn vui lòng chọn kiểu sản phẩm");
            return false;
        }
        if (StringUtils.isBlank(productDto.getProductName())) {
            FacesUtil.addErrorMessage("Bạn vui lòng nhập tên sản phẩm");
            return false;
        } else {
            productDto.setProductName(removeSpaceOfString(productDto.getProductName()));
        }

        //check validate code
        Long countCode = productRepository.findMaxCountCode() == null ? 0 : productRepository.findMaxCountCode();
        if (productDto.getProductId() != null) {
            Product product = productRepository.getByProductId(productDto.getProductId());
            String codeBak = product.getCode();
            if (!productDto.getCode().equals(codeBak) && !productDto.getCode().equals("") && product.getCode() != null && productDto.getCode() != null) {
                productDto.setCode(StringUtil.createCode(productDto.getCode(), Constant.ACRONYM_PRODUCT, countCode));
                if (productDto.getCode() == "") {
                    FacesUtil.addErrorMessage("Mã sản phẩm không hợp lệ");
                    return false;
                }
                List<Product> listCheckCode = productRepository.findByCode(productDto.getCode());
                if (listCheckCode.size() > 1) {
                    FacesUtil.addErrorMessage("Mã sản phẩm đã tồn tại");
                    return false;
                }
                productDto.setCountCode(StringUtil.createCountCode(productDto.getCode(), Constant.ACRONYM_PRODUCT));
            } else {
                productDto.setCode(codeBak);
            }
            uploadMultipleImageController.getUploadMultipleFileDto().setListToAdd(uploadMultipleImageController.getUploadMultipleFileDto().getListToShow());
        } else {
            productDto.setCode(StringUtil.createCode(productDto.getCode(), Constant.ACRONYM_PRODUCT, countCode));
            if (productDto.getCode().equals("")) {
                FacesUtil.addErrorMessage("Mã sản phẩm không hợp lệ");
                return false;
            }
            List<Product> listCheckCode = productRepository.findByCode(productDto.getCode());
            if (listCheckCode.size() > 0) {
                FacesUtil.addErrorMessage("Mã sản phẩm đã tồn tại");
                return false;
            }
            productDto.setCountCode(StringUtil.createCountCode(productDto.getCode(), Constant.ACRONYM_PRODUCT));
        }

        if (uploadMultipleImageController.getUploadMultipleFileDto().getListToAdd().size() == 0) {
            FacesUtil.addErrorMessage("Bạn vui lòng chọn ảnh cho sản phẩm");
            return false;
        }

        if (productDto.getType() == DbConstant.PRODUCT_TYPE_PARENT) {
            List<Product> productList = productRepository.getAllByTypeAndExpertId(DbConstant.PRODUCT_TYPE_PARENT, productDto.getProductId() != null ? productDto.getProductId() : 0);
            for (Product product : productList) {
                if (productDto.getProductName().equalsIgnoreCase(product.getProductName())) {
                    FacesUtil.addErrorMessage("Sản phẩm đã tồn tại");
                    return false;
                }
            }
        } else {
            if (productDto.getType() == DbConstant.PRODUCT_TYPE_CHILD) {
                if (productDto.getProductParentId() == null) {
                    FacesUtil.addErrorMessage("Bạn vui lòng chọn sản phẩm cha");
                    return false;
                }
            }
            if (productDto.getQuantity() == null) {
                FacesUtil.addErrorMessage("Bạn vui lòng nhập số lượng");
                return false;
            }
            if (StringUtils.isBlank(productDto.getDescription())) {
                FacesUtil.addErrorMessage("Bạn vui lòng nhập mô tả");
                return false;
            }
            if (productDto.getPrice() == null) {
                FacesUtil.addErrorMessage("Bạn vui lòng nhập đơn giá");
                return false;
            }
            if (listOptionSelect.size() == 0) {
                FacesUtil.addErrorMessage("Bạn vui lòng chọn thuộc tính cho sản phẩm");
                return false;
            }
        }
        return true;
    }

    public void onSave() {
        if (!validateData()) {
            return;
        }
        //save to product
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        product.setStatus(DbConstant.PRODUCT_STATUS_ACTIVE);
        product.setUpdateDate(new Date());
        product.setUpdateBy(authorizationController.getAccountDto() == null ? authorizationController.getAccountDto().getAccountId() : 1);
        product = productRepository.save(product);

        // create df prod high light
        if (product.getType() != DbConstant.PRODUCT_TYPE_PARENT) {
            ProductHighLight productHighLight = new ProductHighLight();
            productHighLight.setProductId(product.getProductId());
            productHighLightRepository.save(productHighLight);
        }

        //save image product
        if (productDto.getProductId() != null) {
            List<ProductImage> imageList = productImageRepository.getByProductId(productDto.getProductId());
            for (ProductImage obj : imageList) {
                productImageRepository.deleteById(obj.getProductImageId());
            }
        }
        for (String imagePath : uploadMultipleImageController.getUploadMultipleFileDto().getListToAdd()) {
            ProductImage productImage = new ProductImage();
            productImage.setProductId(product.getProductId());
            productImage.setImagePath(imagePath);
            productImageRepository.save(productImage);
        }
        if (product.getType() != DbConstant.PRODUCT_TYPE_PARENT) {
            //save to product_option_detail
            if (productDto.getProductId() != null) {
                List<ProductOptionDetail> optionDetailListDelete = productOptionDetailRepository.findAllByProductId(productDto.getProductId());
                if (optionDetailListDelete != null) {
                    for (ProductOptionDetail productOptionDetail : optionDetailListDelete) {
                        productOptionDetailRepository.delete(productOptionDetail);
                    }
                }
            }
            String option = "";
            for (int i = 0; i < listOptionSelect.size(); i++) {
                Long productOptionId = ValueUtil.getLongByObject(listOptionSelect.get(i));
                ProductOptionDetail productOptionDetail = new ProductOptionDetail();
                productOptionDetail.setProductId(product.getProductId());
                productOptionDetail.setProductOptionId(productOptionId);
                productOptionDetailRepository.save(productOptionDetail);

                //set productOptionName
                ProductOption productOption = productOptionRepository.getByProductOptionId(productOptionDetail.getProductOptionId());
                if (option == "") {
                    option += productOption.getOptionName();
                } else {
                    option += " - " + productOption.getOptionName();
                }
                productDto.setProductNameToShow(product.getProductName() +  "(" + option + ")");
            }

            //save to product_link
            if (product.getType() == DbConstant.PRODUCT_TYPE_CHILD) {
                ProductLink productLinkDelete = productLinkRepository.getByChildId(product.getProductId());
                if (productLinkDelete != null) {
                    productLinkRepository.delete(productLinkDelete);
                }
                ProductLink productLink = new ProductLink();
                productLink.setChildId(product.getProductId());
                productLink.setParentId(productDto.getProductParentId());
                productLinkRepository.save(productLink);
            }
        }

        if (productDto.getProductId() == null && productDto.getType() != DbConstant.PRODUCT_TYPE_PARENT) {
            //send notification
            SendNotification sendNotification = new SendNotification();
            sendNotification.setAccountId(authorizationController.getAccountDto().getAccountId());
            sendNotification.setContent("Có sản phẩm mới: <b>" + productDto.getProductNameToShow() != null ? productDto.getProductNameToShow() : product.getProductName() + "</b>");
            sendNotification.setStatus(DbConstant.SNOTIFICATION_STATUS_ACTIVE);
            sendNotification.setObjectId(product.getProductId());
            sendNotification.setType(DbConstant.NOTIFICATION_TYPE_PRODUCT);
            sendNotification.setCreateDate(new Date());
            sendNotification.setUpdateDate(new Date());
            sendNotificationRepository.save(sendNotification);
            //receive notification
            List<Account> customerList = getAccountRepository().findAccountByRoleId(DbConstant.ROLE_ID_USER);
            for (Account customer : customerList) {
                ReceiveNotification receiveNotification = new ReceiveNotification();
                receiveNotification.setAccountId(customer.getAccountId());
                receiveNotification.setSendNotificationId(sendNotification.getSendNotificationId());
                receiveNotification.setStatus(DbConstant.NOTIFICATION_STATUS_NOT_SEEN);
                receiveNotification.setStatusBell(DbConstant.NOTIFICATION_STATUS_BELL_NOT_SEEN);
                receiveNotification.setCreateDate(new Date());
                receiveNotification.setUpdateDate(new Date());
                notificationRepository.save(receiveNotification);
            }
        }

        FacesUtil.addSuccessMessage("Lưu thành công.");
        FacesUtil.closeDialog("dialogInsertUpdate");
        resetAll();
    }

    public void showUpdatePopup(ProductDto resultDto) {
        BeanUtils.copyProperties(resultDto, productDto);
        productDto.setProductImages(new LinkedHashSet<>());
        productDto.setProductImages(productImageRepository.getImagePathByProductId(productDto.getProductId()));
        uploadMultipleImageController.resetAll(productDto.getProductImages());
        productDto.setOptionDetails(new ArrayList<>());
        productDto.setOptionDetails(productOptionDetailRepository.findAllByProductId(productDto.getProductId()));
        listOptionSelect = new ArrayList<>();
        for (ProductOptionDetail obj : productDto.getOptionDetails()) {
            listOptionSelect.add(obj.getProductOptionId());
        }
        if (productDto.getType() == DbConstant.PRODUCT_TYPE_CHILD) {
            ProductLink productLink = productLinkRepository.getByChildId(productDto.getProductId());
            productDto.setProductParentId(productLink != null ? productLink.getParentId() : null);
        }
    }

    public void onDelete(ProductDto resultDto) {
        resultDto.setStatus(DbConstant.PRODUCT_STATUS_INACTIVE);
        Product product = new Product();
        BeanUtils.copyProperties(resultDto, product);
        productRepository.save(product);
        FacesUtil.addSuccessMessage("Xóa thành công.");
        resetAll();
    }

    public void onChangeParentMenu(Long parentId) {
        Product parent = productRepository.getByProductId(parentId);
        productDto.setCategoryId(parent.getCategoryId());
        productDto.setBrandId(parent.getBrandId());
        FacesUtil.updateView("dlForm");
    }

    public String removeSpaceOfString(String str) {
        return str.trim().replaceAll("[\\s|\\u00A0]+", " ");
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }
}
