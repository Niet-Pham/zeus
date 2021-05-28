package vn.tech.website.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Orders extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long ordersId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "code")
    private String code;

    @Column(name = "count_code")
    private Long countCode;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "note")
    private String note;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "shipping_id")
    private Long shippingId;

    @Column(name = "shipping")
    private Double shipping;

    @Column(name = "status")
    private Integer status;
}
