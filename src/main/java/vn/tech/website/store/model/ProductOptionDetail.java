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
@Table(name = "product_option_detail")
public class ProductOptionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_detail_id")
    private Long productOptionDetailId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_option_id")
    private Long productOptionId;
}
