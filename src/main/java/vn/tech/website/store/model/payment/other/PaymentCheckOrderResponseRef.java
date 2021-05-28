package vn.tech.website.store.model.payment.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "payment_check_order_response_ref")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentCheckOrderResponseRef extends BaseModel {
    private static final long serialVersionUID = 6460826906684140251L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_check_order_response_ref_id")
    private Long paymentCheckOrderResponseRefId;

    @Column(name = "token_code")
    private String token_code;

    @Column(name = "version")
    private String version;

    @Column(name = "order_code")
    private String order_code;

    @Column(name = "order_description")
    private String order_description;

    @Column(name = "amount")
    private String amount;

    @Column(name = "sender_fee")
    private Integer sender_fee;

    @Column(name = "receiver_fee")
    private Integer receiver_fee;

    @Column(name = "currency")
    private String currency;

    @Column(name = "return_url")
    private String return_url;

    @Column(name = "cancel_url")
    private String cancel_url;

    @Column(name = "notify_url")
    private String notify_url;

    @Column(name = "status")
    private Integer status;

    @Column(name = "payment_method_code")
    private String payment_method_code;

    @Column(name = "payment_method_name")
    private String payment_method_name;

    @Column(name = "payment_check_order_response_id")
    private Long paymentCheckOrderResponseId;
}
