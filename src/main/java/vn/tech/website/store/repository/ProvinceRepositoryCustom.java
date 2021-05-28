package vn.tech.website.store.repository;

import vn.tech.website.store.dto.base.ProvinceSearchDto;
import vn.tech.website.store.model.Province;

import java.util.List;

public interface ProvinceRepositoryCustom {
    List<Province> search(ProvinceSearchDto provinceSearchDto);

    Long countSearch(ProvinceSearchDto provinceSearchDto);

    void deleteAllRecords();
}
