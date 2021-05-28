package vn.tech.website.store.dto.payment;

import lombok.Getter;
import lombok.Setter;
import vn.tech.website.store.dto.BaseSearchDto;

import java.util.Date;

@Getter
@Setter
public class NotifyPaymentSearchDto extends BaseSearchDto {
    private Long storeRegisterId;
    private Long storeRegisterCode;
    private Long accountId;
    private Long assetId;
    private String assetName;
    private Long regulationId;
    private String regulationCode;
    private Integer status;
    private Date fromDate;
    private Date toDate;
}
