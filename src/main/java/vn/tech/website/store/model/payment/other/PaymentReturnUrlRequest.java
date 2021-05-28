package vn.tech.website.store.model.payment.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.BaseModel;
import vn.tech.website.store.util.payment.PaymentVariableUtil;

import javax.persistence.*;

@Entity
@Table(name = "payment_return_url_request")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentReturnUrlRequest extends BaseModel {
    private static final long serialVersionUID = 4704441432976382888L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_return_url_request_id")
    private Long payment_return_url_request_id;

    @Column(name = "function")
    private String function;

    @Column(name = "merchant_site_code")
    private String merchant_site_code;

    @Column(name = "order_code")
    private String order_code;

    @Column(name = "order_description")
    private String order_description;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "buyer_fullname")
    private String buyer_fullname;

    @Column(name = "buyer_email")
    private String buyer_email;

    @Column(name = "buyer_mobile")
    private String buyer_mobile;

    @Column(name = "buyer_address")
    private String buyer_address;

    @Column(name = "return_url")
    private String return_url;

    @Column(name = "cancel_url")
    private String cancel_url;

    @Column(name = "notify_url")
    private String notify_url;

    @Column(name = "language")
    private String language;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "application_id")
    private Integer application_id;

    @Column(name = "payment_id")
    private Long paymentId;

    public PaymentReturnUrlRequest(String function, String merchant_site_code, String order_code, String order_description, Long amount, String currency, String buyer_fullname, String buyer_email, String buyer_mobile, String buyer_address, String return_url, String cancel_url, String notify_url, String language, String checksum, Integer application_id) {
        this.function = function;
        this.merchant_site_code = merchant_site_code;
        this.order_code = order_code;
        this.order_description = order_description;
        this.amount = amount;
        this.currency = currency;
        this.buyer_fullname = buyer_fullname;
        this.buyer_email = buyer_email;
        this.buyer_mobile = buyer_mobile;
        this.buyer_address = buyer_address;
        this.return_url = return_url;
        this.cancel_url = cancel_url;
        this.notify_url = notify_url;
        this.language = language;
        this.checksum = checksum;
        this.application_id = application_id;
    }

    public PaymentReturnUrlRequest(String function, String merchant_site_code, String checksum, Integer application_id) {
        this.function = function;
        this.merchant_site_code = merchant_site_code;
        this.checksum = checksum;
        this.application_id = application_id;
    }

    @Override
    public String toString() {
        return PaymentVariableUtil.getApiWalletMerchantSiteCode() + '|'
                + order_code + '|'
                + order_description + '|'
                + amount + '|'
                + PaymentVariableUtil.getApiWalletCurrency() + '|'
                + buyer_fullname + '|'
                + buyer_email + '|'
                + buyer_mobile + '|'
                + buyer_address + '|'
                + PaymentVariableUtil.getApiWalletReturnUrl() + '|'
                + PaymentVariableUtil.getApiWalletCancelUrl() + '|'
                + PaymentVariableUtil.getApiWalletNotifyUrl() + '|'
                + PaymentVariableUtil.getApiWalletLanguage() + '|'
                + PaymentVariableUtil.getApiWalletMerchantPassCode();
    }
}
