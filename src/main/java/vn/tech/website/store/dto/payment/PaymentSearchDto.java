package vn.tech.website.store.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.dto.BaseSearchDto;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSearchDto extends BaseSearchDto {
    private String code;
    private Integer status;
    private String customerName;
    private String phone;
    private Integer statusInit;
}
