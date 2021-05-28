package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.tech.website.store.model.payment.NotifyPayment;

import java.util.List;

public interface NotifyPaymentRepository extends CrudRepository<NotifyPayment, Long>, NotifyPaymentRepositoryCustom {
    @Query("select count (np.notifyPaymentId) from NotifyPayment np where np.assetId = :assetId and np.regulationId = :regulationId and np.accountId = :accountId")
    Integer countNotifyPaymentId(@Param("assetId") Long assetId, @Param("regulationId") Long regulationId, @Param("accountId") Long accountId);

    List<NotifyPayment> findAllByStoreRegisterId(Long storeRegisterId);
}
