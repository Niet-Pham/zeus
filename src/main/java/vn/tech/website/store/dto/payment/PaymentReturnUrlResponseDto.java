package vn.tech.website.store.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentReturnUrlResponseDto {
    private String tokenCode;
    private String version;
    private String orderCode;
    private String orderDescription;
    private String amount;
    private Integer senderFee;
    private Integer recevierFee;
    private String currency;
    private String returnUrl;
    private String cancelUrl;
    private String notifyUrl;
    private Integer status;
    private String paymentMethodCode;
    private String paymentMethodName;
}
