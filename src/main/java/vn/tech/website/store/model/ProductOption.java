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
@Table(name = "product_option")
public class ProductOption extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long productOptionId;

    @Column(name = "name")
    private String optionName;

    @Column(name = "value")
    private String optionValue;

    @Column(name = "type")
    private Integer type;

    @Column(name = "status")
    private Integer status;
}
