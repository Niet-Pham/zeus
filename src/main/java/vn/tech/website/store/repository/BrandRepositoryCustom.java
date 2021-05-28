package vn.tech.website.store.repository;

import vn.tech.website.store.dto.BrandDto;
import vn.tech.website.store.dto.BrandSearchDto;

import java.util.List;

public interface BrandRepositoryCustom {
    List<BrandDto> search(BrandSearchDto searchDto);

    Long countSearch(BrandSearchDto searchDto);
}
