package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.Shipping;

import java.util.List;

public interface ShippingRepository extends CrudRepository<Shipping, Long>, ShippingRepositoryCustom {
    @Query("select s from Shipping s")
    List<Shipping> findAllShipping();

    Shipping getByShippingId(Long shippingId);
}
