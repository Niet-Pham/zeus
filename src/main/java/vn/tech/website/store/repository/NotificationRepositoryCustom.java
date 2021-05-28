package vn.tech.website.store.repository;

import vn.tech.website.store.dto.NotificationSearchDto;
import vn.tech.website.store.dto.NotificationDto;

import java.util.List;

public interface NotificationRepositoryCustom {
    List<NotificationDto> search(NotificationSearchDto searchDto);

    Long countRecordNotSeen(NotificationSearchDto searchDto);
}
