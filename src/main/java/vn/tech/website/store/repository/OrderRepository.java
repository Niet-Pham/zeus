package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.Orders;

public interface OrderRepository extends CrudRepository<Orders, Long>, OrderRepositoryCustom {

    @Query("select max(o.countCode) from Orders o")
    Long findMaxCountCode();

    Orders getByOrdersId(Long orderId);
}
