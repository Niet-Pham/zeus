package vn.tech.website.store.model.payment.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "payment_return_url_response")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentReturnUrlResponse extends BaseModel {
    private static final long serialVersionUID = -7920419477884430347L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_return_url_response_id")
    private Long payment_return_url_response_id;

    @Column(name = "result_code")
    private String result_code;

    @Column(name = "result_message")
    private String result_message;

    @Column(name = "store_register_id")
    private Long storeRegisterId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "payment_return_url_request_id")
    private Long paymentReturnUrlRequestId;
}
