package vn.tech.website.store.model.payment.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "payment_check_order_request")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentCheckOrderRequest extends BaseModel {
    private static final long serialVersionUID = 8827634143163562216L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_check_order_id")
    private Long paymentCheckOrderId;

    @Column(name = "function")
    private String function;

    @Column(name = "merchant_site_code")
    private String merchantSiteCode;

    @Column(name = "token_code")
    private String tokenCode;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "application_id")
    private Integer applicationId;

    @Column(name = "payment_return_url_request_id")
    private Long paymentReturnUrlRequestId;
}
