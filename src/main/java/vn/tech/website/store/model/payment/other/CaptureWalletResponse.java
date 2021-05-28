package vn.tech.website.store.model.payment.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CaptureWalletResponse extends PaymentReturnUrlResponse {
    private PaymentReturnUrlResponseRef result_data;

    public PaymentReturnUrlResponse getParent() {
        PaymentReturnUrlResponse paymentReturnUrlResponse = new PaymentReturnUrlResponse();
        paymentReturnUrlResponse.setResult_code(getResult_code());
        paymentReturnUrlResponse.setResult_message(getResult_message());

        return paymentReturnUrlResponse;
    }
}
