package vn.tech.website.store.model.payment;

import lombok.*;
import vn.tech.website.store.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "receipt_management")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReceiptManagement extends BaseModel {
    private static final long serialVersionUID = 6621292032690611926L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_management_id")
    private Long receiptManagementId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "code")
    private String code;

    @Column(name = "payer_fullname")
    private String payerFullname;

    @Column(name = "payer_address")
    private String payerAddress;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "content_payment")
    private String contentPayment;

    @Column(name = "status")
    private boolean status;
}
