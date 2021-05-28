package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.ProductOption;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDto extends ProductOption {
    private String typeOptionString;
}
