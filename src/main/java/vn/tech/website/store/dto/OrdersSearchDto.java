package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdersSearchDto extends BaseSearchDto {
    private String code;
    private Integer status;
    private String customerName;
    private String phone;
    private Integer statusInit;
    private Long accountId;
    private Long ordersId;
}
