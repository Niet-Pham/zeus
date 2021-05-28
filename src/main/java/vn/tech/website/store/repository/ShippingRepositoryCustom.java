package vn.tech.website.store.repository;

import vn.tech.website.store.dto.ShippingSearchDto;

import java.util.List;

public interface ShippingRepositoryCustom {
    List search(ShippingSearchDto searchDto);

    Long countSearch(ShippingSearchDto searchDto);
}
