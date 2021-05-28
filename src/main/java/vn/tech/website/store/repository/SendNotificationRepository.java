package vn.tech.website.store.repository;

import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.SendNotification;

public interface SendNotificationRepository extends CrudRepository<SendNotification, Long> {
    SendNotification getBySendNotificationId(Long sendNotificationId);
}
