package vn.tech.website.store.repository;

import vn.tech.website.store.dto.ChartModelDto;
import vn.tech.website.store.dto.LineChartModelDto;
import vn.tech.website.store.dto.payment.PaymentDto;
import vn.tech.website.store.dto.payment.PaymentSearchDto;

import java.util.List;

public interface PaymentsRepositoryCustom {
    List<PaymentDto> search(PaymentSearchDto paymentSearchDto);
    Long countSearch(PaymentSearchDto paymentSearchDto);
    List<ChartModelDto> getDataModelChart(Integer year);
    LineChartModelDto getDataLineChart(Integer year, Long catePhone, Long catePc, Long cateWireless, Long cateWatch);
}
