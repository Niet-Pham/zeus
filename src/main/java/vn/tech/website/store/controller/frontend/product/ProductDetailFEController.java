package vn.tech.website.store.controller.frontend.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.frontend.BaseFEController;
import vn.tech.website.store.dto.OrdersDetailDto;
import vn.tech.website.store.dto.ProductDto;
import vn.tech.website.store.dto.ProductOptionDetailDto;
import vn.tech.website.store.dto.ProductOptionDetailSearchDto;
import vn.tech.website.store.model.Product;
import vn.tech.website.store.model.ProductImage;
import vn.tech.website.store.model.ProductOptionDetail;
import vn.tech.website.store.repository.ProductImageRepository;
import vn.tech.website.store.repository.ProductOptionDetailRepository;
import vn.tech.website.store.repository.ProductRepository;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.FacesUtil;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named
@Scope(value = "session")
@Getter
@Setter
public class ProductDetailFEController extends BaseFEController {
    @Inject
    private HttpServletRequest request;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductOptionDetailRepository productOptionDetailRepository;

    private Long parentId;
    private Long productId;
    private Integer quantity;

    private ProductDto productDtoView;
    private ProductOptionDetailSearchDto searchDto;
    private Map<Long, ProductDto> childProductMap;
    private List<ProductImage> productImageList;
    private List<ProductOptionDetailDto> productOptionDetailDtoList;
    private List<SelectItem> optionList;
    private Map<String, OrdersDetailDto> mapAddToCart;

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAll();
        }
    }

    public void resetAll() {
        quantity = 1;

        productDtoView = new ProductDto();
        productImageList = new ArrayList<>();
        if (!childProductMap.isEmpty()) {
            ProductDto defaultProduct = productId == null ? childProductMap.values().stream().findFirst().get() : childProductMap.get(productId);
            BeanUtils.copyProperties(defaultProduct, productDtoView);
            productImageList = productImageRepository.findAllByProductId(defaultProduct.getProductId());
        }

        getOptionProduct();
    }

    public void getOptionProduct() {
        searchDto = new ProductOptionDetailSearchDto();
        searchDto.setParentId(parentId);
        searchDto.setProductIdTypeNone(productDtoView.getProductId());
        productOptionDetailDtoList = new ArrayList<>();
        optionList = new ArrayList<>();
        List<Long> checkProductIds = new ArrayList<>();
        productOptionDetailDtoList = productOptionDetailRepository.searchOption(searchDto);
        for (int i = 0; i < productOptionDetailDtoList.size(); i++) {
            boolean check = false;
            StringBuilder label = new StringBuilder();
            for (int j = 1; j < productOptionDetailDtoList.size(); j++) {
                if (j <= i || checkProductIds.contains(productOptionDetailDtoList.get(i).getProductId()) || !productOptionDetailDtoList.get(i).getProductId().equals(productOptionDetailDtoList.get(j).getProductId())) {
                    continue;
                }
                if (!check) {
                    label.append(productOptionDetailDtoList.get(i).getProductOptionName());
                    check = true;
                }
                label.append(" - ").append(productOptionDetailDtoList.get(j).getProductOptionName());
            }
            if (label.toString().compareTo("") != 0) {
                optionList.add(new SelectItem(productOptionDetailDtoList.get(i).getProductId(), label.toString()));
                checkProductIds.add(productOptionDetailDtoList.get(i).getProductId());
            }
        }
        if (optionList.isEmpty()) {
            for (ProductOptionDetailDto o : productOptionDetailDtoList) {
                optionList.add(new SelectItem(o.getProductId(), o.getProductOptionName()));
                checkProductIds.add(o.getProductId());
            }
        }
    }

    public void onChangeItem(Long productId) {
        productDtoView = childProductMap.get(productId);
        productImageList = productImageRepository.findAllByProductId(productId);
        FacesUtil.updateView("content-detail");
    }

    public void addToCart() {
        addToCartCommon(productDtoView, quantity);
    }

    public void addToCartCommon(ProductDto resultDto, Integer quantity) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("cartList") == null) {
            mapAddToCart = new LinkedHashMap<>();
            session.setAttribute("cartList", mapAddToCart);
        }
        mapAddToCart = (Map<String, OrdersDetailDto>) session.getAttribute("cartList");

        OrdersDetailDto dto = new OrdersDetailDto();
        BeanUtils.copyProperties(resultDto, dto);
        dto.setProductId(resultDto.getProductId());
        dto.setQuantity(Long.parseLong(quantity.toString()));
        dto.setAmount(resultDto.getPriceAfterDiscount() == null ? resultDto.getPrice() : resultDto.getPriceAfterDiscount());
        dto.setCodeProduct(resultDto.getCode());
        dto.setProductDto(resultDto);

        if (mapAddToCart.size() == 0) {
            mapAddToCart.put(resultDto.getCode(), dto);
        } else {
            boolean checkExits = true;
            for (OrdersDetailDto obj : mapAddToCart.values()) {
                if (dto.getProductId().equals(obj.getProductId())) {
                    Long qty = obj.getQuantity() + quantity;
                    if (qty > resultDto.getQuantity()) {
                        setErrorForm("Số lượng trong kho không đủ để mua thêm, vui lòng quay lại sau.");
                        return;
                    }
                    obj.setQuantity(qty);
                    checkExits = false;
                    break;
                }
            }
            if (checkExits) {
                mapAddToCart.put(resultDto.getCode(), dto);
            }
        }
        session.setAttribute("cartList", mapAddToCart);
        setSuccessForm("Thêm sản phẩm \"" + resultDto.getProductName() + "\" vào giỏ hàng thành công");
    }

    @Override
    protected String getMenuId() {
        return null;
    }
}
