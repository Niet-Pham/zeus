package vn.tech.website.store.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.tech.website.store.model.District;

import java.util.List;

@Getter
@Setter
public class DistrictDto extends District {
    private static final long serialVersionUID = 3084707787010033176L;

    private String provinceName;
    private List<District> districtList;

    public DistrictDto() {
    }

}
