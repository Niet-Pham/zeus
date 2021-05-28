package vn.tech.website.store.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.dto.OrdersDto;
import vn.tech.website.store.model.OrdersDetail;
import vn.tech.website.store.model.Payments;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto extends Payments {
    private Double amountProduct;
    private String userName;
    private String shippingName;
    private OrdersDto ordersDto = new OrdersDto();
}
