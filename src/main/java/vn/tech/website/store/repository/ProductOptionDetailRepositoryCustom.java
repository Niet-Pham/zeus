package vn.tech.website.store.repository;

import vn.tech.website.store.dto.ProductOptionDetailDto;
import vn.tech.website.store.dto.ProductOptionDetailSearchDto;

import java.util.List;

public interface ProductOptionDetailRepositoryCustom  {
    List<ProductOptionDetailDto> searchOption(ProductOptionDetailSearchDto searchDto);
}
