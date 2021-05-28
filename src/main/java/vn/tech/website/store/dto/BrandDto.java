package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.Brand;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto extends Brand {
    private String nameUpdate;
}
