package vn.tech.website.store.repository;

import vn.tech.website.store.dto.ProductOptionDto;
import vn.tech.website.store.dto.ProductOptionSearchDto;

import java.util.List;

public interface ProductOptionRepositoryCustom {
    List<ProductOptionDto> search(ProductOptionSearchDto searchDto);

    Long countSearch(ProductOptionSearchDto searchDto);
}
