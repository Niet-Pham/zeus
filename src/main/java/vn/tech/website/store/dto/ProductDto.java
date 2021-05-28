package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.Product;
import vn.tech.website.store.model.ProductImage;
import vn.tech.website.store.model.ProductOptionDetail;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto extends Product {
    private String brandName;
    private String categoryName;
    private Long productParentId;
    private List<ProductOptionDetail> optionDetails;
    private Set<String> productImages;
    private String imageToShow;
    private Double priceAfterDiscount;
    private Long totalQtyParent;
    private String productNameToShow;
}
