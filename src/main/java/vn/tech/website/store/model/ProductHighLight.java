package vn.tech.website.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_highlight")
public class ProductHighLight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_highlight_id")
    private Long productHighlightId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "date_add")
    private Date dateAdd = new Date();

    @Column(name = "point")
    private Integer point = 0;
}
