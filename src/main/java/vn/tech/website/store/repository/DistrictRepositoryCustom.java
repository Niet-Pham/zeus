package vn.tech.website.store.repository;

import vn.tech.website.store.dto.base.DistrictDto;
import vn.tech.website.store.dto.base.DistrictSearchDto;

import java.util.List;

public interface DistrictRepositoryCustom {

    List<DistrictDto> search(DistrictSearchDto districtSearchDto);

    Long countSearch(DistrictSearchDto districtSearchDto);

    void deleteAllRecords();
}
