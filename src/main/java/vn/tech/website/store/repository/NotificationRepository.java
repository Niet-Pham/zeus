package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import vn.tech.website.store.model.ReceiveNotification;

public interface NotificationRepository extends CrudRepository<ReceiveNotification,Long>, NotificationRepositoryCustom {
    @Transactional
    @Modifying
    @Query("UPDATE ReceiveNotification r SET r.statusBell = :statusBell WHERE r.accountId = :accountId")
    void changeStatusBell(@Param("accountId") Long accountId, @Param("statusBell") boolean statusBell);
}
