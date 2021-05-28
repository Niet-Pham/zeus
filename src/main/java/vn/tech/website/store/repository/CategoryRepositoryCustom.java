package vn.tech.website.store.repository;

import vn.tech.website.store.dto.CategoryDto;
import vn.tech.website.store.dto.CategorySearchDto;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<CategoryDto> search(CategorySearchDto searchDto);

    Long countSearch(CategorySearchDto searchDto);
}
