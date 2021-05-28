package vn.tech.website.store.repository;

import vn.tech.website.store.dto.base.CommuneDto;
import vn.tech.website.store.dto.base.CommuneSearchDto;

import java.util.List;

public interface CommuneRepositoryCustom {
    List<CommuneDto> search(CommuneSearchDto communeSearchDto);

    Long countSearch(CommuneSearchDto communeSearchDto);

    void deleteAllRecords();
}
