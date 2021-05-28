package vn.tech.website.store.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.dto.BaseSearchDto;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptManagementSearchDto extends BaseSearchDto {
    private String code;
    private String payerFullname;
    private String payerAddress;
    private Float amount;
    private String contentPayment;
    private Date createDate;
    private Long paymentId;
    private Date startDate;
    private Date endDate;
    private Integer status;
}
