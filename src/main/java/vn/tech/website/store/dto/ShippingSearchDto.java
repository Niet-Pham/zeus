package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.Shipping;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingSearchDto extends BaseSearchDto {
    private Long shippingId;
    private String name;
    private String code;
    private Integer price;
    private String detail;
    private String path;
}
