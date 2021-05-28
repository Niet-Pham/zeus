package vn.tech.website.store.model.payment.momo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "momo_wallet_request")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MomoWalletRequest extends BaseModel {
    private static final long serialVersionUID = -7956167319665451143L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "momo_wallet_request_id")
    private Long momoWalletRequestId;

    @Column(name = "partner_code")
    private String partnerCode;

    @Column(name = "access_key")
    private String accessKey;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "amount")
    private String amount;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "order_infor")
    private String orderInfor;

    @Column(name = "return_url")
    private String returnUrl;

    @Column(name = "notify_url")
    private String notifyUrl;

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "extra_data")
    private String extraData;
}


