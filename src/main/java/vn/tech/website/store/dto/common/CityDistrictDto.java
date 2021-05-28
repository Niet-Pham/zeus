package vn.tech.website.store.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityDistrictDto {
    private static final long serialVersionUID = 3097108909843668671L;

    private Long provinceId;
    private Long districtId;
    private Long communeId;

    private String provinceName;
    private String districtName;
    private String communeName;

    public CityDistrictDto(Long provinceId, Long districtId, Long communeId) {
        this.provinceId = provinceId;
        this.districtId = districtId;
        this.communeId = communeId;
    }

    public CityDistrictDto(String provinceName, String districtName, String communeName) {
        this.provinceName = provinceName;
        this.districtName = districtName;
        this.communeName = communeName;
    }

    @Override
    public String toString() {
        return communeName + " - " + districtName + " - " + provinceName;
    }
}
