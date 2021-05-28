package vn.tech.website.store.model.payment.other;

import lombok.Getter;
import lombok.Setter;
import vn.tech.website.store.util.payment.PaymentVariableUtil;

@Getter
@Setter
public class CaptureWalletRequest extends PaymentReturnUrlRequest {
    private String merchant_passCode;
    private String token_code;

    public CaptureWalletRequest() {
    }

    public CaptureWalletRequest(String function, String merchant_site_code, String order_code, String order_description, Long amount, String currency, String buyer_fullname, String buyer_email, String buyer_mobile, String buyer_address, String return_url, String cancel_url, String notify_url, String language, String checksum, Integer application_id) {
        super(function, merchant_site_code, order_code, order_description, amount, currency, buyer_fullname, buyer_email, buyer_mobile, buyer_address, return_url, cancel_url, notify_url, language, checksum, application_id);
    }

    public CaptureWalletRequest(String function, String merchant_site_code, String checksum, Integer application_id, String token_code) {
        super(function, merchant_site_code, checksum, application_id);
        this.token_code = token_code;
    }

    public PaymentReturnUrlRequest getParent() {
        PaymentReturnUrlRequest paymentReturnUrlRequest = new PaymentReturnUrlRequest();

        paymentReturnUrlRequest.setPayment_return_url_request_id(getPayment_return_url_request_id());
        paymentReturnUrlRequest.setFunction(getFunction());
        paymentReturnUrlRequest.setMerchant_site_code(getMerchant_site_code());
        paymentReturnUrlRequest.setOrder_code(getOrder_code());
        paymentReturnUrlRequest.setOrder_description(getOrder_description());
        paymentReturnUrlRequest.setAmount(getAmount());
        paymentReturnUrlRequest.setCurrency(getCurrency());
        paymentReturnUrlRequest.setBuyer_fullname(getBuyer_fullname());
        paymentReturnUrlRequest.setBuyer_email(getBuyer_email());
        paymentReturnUrlRequest.setBuyer_mobile(getBuyer_mobile());
        paymentReturnUrlRequest.setBuyer_address(getBuyer_address());
        paymentReturnUrlRequest.setReturn_url(getReturn_url());
        paymentReturnUrlRequest.setCancel_url(getCancel_url());
        paymentReturnUrlRequest.setNotify_url(getNotify_url());
        paymentReturnUrlRequest.setLanguage(getLanguage());
        paymentReturnUrlRequest.setChecksum(getChecksum());
        paymentReturnUrlRequest.setApplication_id(getApplication_id());

        return paymentReturnUrlRequest;
    }

    public String toStringCheckOrder() {
        return PaymentVariableUtil.getApiWalletMerchantSiteCode() + '|'
                + token_code + '|'
                + PaymentVariableUtil.getApiWalletMerchantPassCode();
    }
}
