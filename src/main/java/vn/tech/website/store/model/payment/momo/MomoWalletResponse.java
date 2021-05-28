package vn.tech.website.store.model.payment.momo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "momo_wallet_response")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MomoWalletResponse extends BaseModel {
    private static final long serialVersionUID = 2454189786368545172L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "momo_wallet_response_id")
    private Long momoWalletResponseId;

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

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "trans_id")
    private String transId;

    @Column(name = "error_code")
    private int errorCode;

    @Column(name = "message")
    private String message;

    @Column(name = "local_message")
    private String localMessage;

    @Column(name = "pay_type")
    private String payType;

    @Column(name = "response_time")
    private String responseTime;

    @Column(name = "extra_data")
    private String extraData;
}
