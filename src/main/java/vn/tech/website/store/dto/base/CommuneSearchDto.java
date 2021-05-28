package vn.tech.website.store.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.dto.BaseSearchDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommuneSearchDto extends BaseSearchDto {
    private static final long serialVersionUID = -2480355564042472783L;

    private Long CommuneId;
    private Long districtId;
    private Long provinceId;
    private String code;
    private String name;
    private Long status;


}
