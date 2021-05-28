package vn.tech.website.store.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.dto.OrdersDetailDto;
import vn.tech.website.store.dto.ProductDto;
import vn.tech.website.store.dto.payment.PaymentDto;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportExcelDto {
    private Date fromDate;
    private Date toDate;
    private Date reportDate;
    private String exportDate;
    private String exportTypeData;
    private String fromDateString;
    private String toDateString;
    private String exportDateString;
    private int totalContract;

    private PaymentDto reportPayment;
    private List<OrdersDetailDto> ordersDetailDtoList;
}
