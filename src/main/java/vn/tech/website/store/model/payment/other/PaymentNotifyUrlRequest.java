package vn.tech.website.store.model.payment.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "payment_notify_url_request")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentNotifyUrlRequest extends BaseModel {
    private static final long serialVersionUID = 3754061816507693561L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_notify_url_request_id")
    private Long paymentNotifyUrlRequestId;

    @Column(name = "token_code")
    private String tokenCode;

    @Column(name = "version")
    private String version;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "order_description")
    private String orderDesciption;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payment_method_code")
    private String paymentMethodCode;

    @Column(name = "payment_method_name")
    private String paymentMethodName;

    @Column(name = "sender_fee")
    private Float senderFee;

    @Column(name = "receiver_fee")
    private Float receiverFee;

    @Column(name = "status")
    private String status;

    @Column(name = "return_url")
    private String returnUrl;

    @Column(name = "cancel_url")
    private String cancelUrl;

    @Column(name = "notify_url")
    private String notifyUrl;

    @Column(name = "checksum")
    private String checksum;
}
