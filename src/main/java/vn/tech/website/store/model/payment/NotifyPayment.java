package vn.tech.website.store.model.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "notify_payment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotifyPayment extends BaseModel {
    private static final long serialVersionUID = 6848419231088386622L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notify_payment_id")
    private Long notifyPaymentId;

    @Column(name = "store_register_id")
    private Long storeRegisterId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "asset_id")
    private Long assetId;

    @Column(name = "regulation_id")
    private Long regulationId;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "status")
    private Integer status;
}
