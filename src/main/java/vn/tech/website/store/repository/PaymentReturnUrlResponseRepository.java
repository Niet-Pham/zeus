package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.tech.website.store.model.payment.other.PaymentReturnUrlResponse;

public interface PaymentReturnUrlResponseRepository extends CrudRepository<PaymentReturnUrlResponse, Long> {
    @Query("select count (prur.payment_return_url_response_id) from PaymentReturnUrlResponse prur where prur.paymentReturnUrlRequestId = :paymentReturnUrlRequestId")
    Integer countStoreRegisterResponse(@Param("paymentReturnUrlRequestId") Long paymentReturnUrlRequestId);

    @Query("select prur.status from PaymentReturnUrlResponse prur where prur.storeRegisterId = :storeRegisterId")
    Integer getStatusResponse(@Param("storeRegisterId") Long storeRegisterId);
}
