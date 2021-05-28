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
public class RoleSearchDto extends BaseSearchDto {
    private static final long serialVersionUID = 7501154597695666646L;

    private Integer roleId;
    private String code;
    private String name;
    private Long status;
}

