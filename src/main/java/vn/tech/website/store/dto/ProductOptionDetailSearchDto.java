package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.ProductOptionDetail;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDetailSearchDto extends BaseSearchDto {
    private Long parentId;
    private Long productIdTypeNone;
}
