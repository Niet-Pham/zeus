package vn.tech.website.store.repository;

import vn.tech.website.store.dto.payment.NotifyPaymentDto;
import vn.tech.website.store.dto.payment.NotifyPaymentSearchDto;

import java.util.List;

public interface NotifyPaymentRepositoryCustom {
    List<NotifyPaymentDto> search(NotifyPaymentSearchDto searchDto);
    Long countSearch(NotifyPaymentSearchDto searchDto);
}
