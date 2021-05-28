package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.Orders;
import vn.tech.website.store.model.OrdersDetail;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDto extends Orders {
    List<OrdersDetailDto> ordersDetailDtoList = new ArrayList<>();
    Double allTotalAmount;
    String statusString;
    Boolean showDetail;
}
