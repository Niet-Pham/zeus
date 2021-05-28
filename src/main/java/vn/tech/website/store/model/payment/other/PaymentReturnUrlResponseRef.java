package vn.tech.website.store.model.payment.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "payment_return_url_response_ref")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentReturnUrlResponseRef extends BaseModel {
    private static final long serialVersionUID = -6415416904261663679L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_return_url_response_ref_id")
    private Long payment_return_url_response_ref_id;

    @Column(name = "checkout_url")
    private String checkout_url;

    @Column(name = "token_code")
    private String token_code;

    @Column(name = "payment_return_url_response_id")
    private Long payment_return_url_response_id;

    public PaymentReturnUrlResponseRef(String checkout_url, String token_code) {
        this.checkout_url = checkout_url;
        this.token_code = token_code;
    }
}
