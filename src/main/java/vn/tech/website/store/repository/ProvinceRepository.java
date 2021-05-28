package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.tech.website.store.dto.common.CityDistrictDto;
import vn.tech.website.store.model.Province;

import java.util.List;

public interface ProvinceRepository extends CrudRepository<Province, Long>, ProvinceRepositoryCustom {
    List<Province> findByName(String name);

    Province findByProvinceId(Long Id);

    @Query(value = "SELECT province_id FROM province ORDER BY province_id DESC LIMIT 1", nativeQuery = true)
    Long getProvinceIdLast();

    @Query("select new vn.tech.website.store.dto.common.CityDistrictDto(p.name, d.name, c.name) from Province p, District d, Commune c where p.provinceId = d.provinceId and d.districtId = c.districtId and p.provinceId = :provinceId and d.districtId = :districtId and c.communeId = :communeId")
    CityDistrictDto getNameCityDistrict(@Param("provinceId") Long provinceId, @Param("districtId") Long districtId, @Param("communeId") Long communeId);
}
