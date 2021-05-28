package vn.tech.website.store.repository;

import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.OrdersDetail;

import java.util.List;

public interface OrderDetailRepository extends CrudRepository<OrdersDetail, Long> {

    List<OrdersDetail> getAllByOrdersId(Long ordersId);
}
