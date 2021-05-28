package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.tech.website.store.model.payment.other.PaymentCheckOrderRequest;

public interface PaymentCheckOrderRequestRepository extends CrudRepository<PaymentCheckOrderRequest, Long> {
    @Query("select prur.paymentReturnUrlRequestId from PaymentReturnUrlResponseRef prurr, PaymentReturnUrlResponse prur where prurr.payment_return_url_response_id = prur.payment_return_url_response_id and prurr.token_code = :tokenCode")
    Long getPaymentReturnUrlRequestId(@Param("tokenCode") String tokenCode);
}
