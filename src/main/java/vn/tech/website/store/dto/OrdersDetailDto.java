package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.OrdersDetail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDetailDto extends OrdersDetail {
    private Double unitPrice;
    private Float discount;
    private Integer positionProduct;
    private String codeProduct;
    private Long maxQuantity;
    private ProductDto productDto = new ProductDto();
    private Long count;
}
