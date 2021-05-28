package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchDto extends BaseSearchDto {
    private String productName;
    private String code;
    private Integer status;
    private String brandName;
    private String categoryName;
    private Long brandId;
    private Long categoryId;
    private Integer type;
    private Integer expertType;

    private Long productId;
    private Long parentId;

    private boolean enableSearchPrice;
    private Long minPrice;
    private Long maxPrice;

    private Long optionColorId;
    private Long optionSizeId;
    private Long optionReleaseId;
    private Long optionOtherId;
    private String optionCondition;
}
