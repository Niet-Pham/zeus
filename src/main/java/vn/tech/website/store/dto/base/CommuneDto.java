package vn.tech.website.store.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.Commune;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommuneDto extends Commune {
    private static final long serialVersionUID = 2155807344162308530L;

    private String districtName;
    private String provinceName;

}
