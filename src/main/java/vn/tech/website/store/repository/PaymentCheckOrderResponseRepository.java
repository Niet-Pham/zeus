package vn.tech.website.store.repository;

import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.payment.other.PaymentCheckOrderResponse;

public interface PaymentCheckOrderResponseRepository extends CrudRepository<PaymentCheckOrderResponse, Long> {
}
