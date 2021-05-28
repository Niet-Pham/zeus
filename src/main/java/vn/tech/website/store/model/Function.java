package vn.tech.website.store.model;

import lombok.Getter;
import lombok.Setter;
import vn.tech.website.store.entity.EAction;
import vn.tech.website.store.entity.EScope;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "function")
public class Function implements Serializable {
    private static final long serialVersionUID = 8518266851765901464L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "function_id")
    private Integer functionId;

    @Column(name = "scope")
    @Enumerated(EnumType.STRING)
    private EScope scope;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private EAction action;

    @Column(name = "name")
    private String name;

}
