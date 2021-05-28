package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.Shipping;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDto extends Shipping {
    private String test;
}
