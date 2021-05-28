package vn.tech.website.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "function_role")
public class FunctionRole implements Serializable {
    private static final long serialVersionUID = -1320090067241864491L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "function_role_id")
    private Integer functionRoleId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "function_id")
    private Integer functionId;
}
