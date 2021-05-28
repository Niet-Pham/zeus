package vn.tech.website.store.dto.base;

import vn.tech.website.store.dto.BaseSearchDto;

public class DistrictSearchDto extends BaseSearchDto {
    private static final long serialVersionUID = -5411903707556380522L;

    private Long districtId;
    private Long provinceId;
    private String code;
    private String name;
    private Long status;

    public DistrictSearchDto() {
    }

    public DistrictSearchDto(Long districtId, Long provinceId, String code, String name, Long status) {
        this.districtId = districtId;
        this.provinceId = provinceId;
        this.code = code;
        this.name = name;
        this.status = status;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
