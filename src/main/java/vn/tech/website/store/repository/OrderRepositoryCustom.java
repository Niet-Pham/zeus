package vn.tech.website.store.repository;

import vn.tech.website.store.dto.OrdersDto;
import vn.tech.website.store.dto.OrdersSearchDto;

import java.util.List;

public interface OrderRepositoryCustom {
    List<OrdersDto> search(OrdersSearchDto searchDto);

    Long countSearch(OrdersSearchDto searchDto);
}
