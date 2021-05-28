package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionSearchDto extends BaseSearchDto {
    private Integer type;
    private String optionName;
    private String optionValue;
    private Integer status;
}
