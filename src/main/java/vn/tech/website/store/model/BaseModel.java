package vn.tech.website.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 3760165139856245368L;

    public BaseModel(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "create_date", updatable = false)
    protected Date createDate;

    @Column(name = "update_date")
    protected Date updateDate;

    @Column(name = "create_by")
    protected Long createBy;

    @Column(name = "update_by")
    protected Long updateBy;

    @PrePersist
    protected void prepareCreateEntity() {
        createDate = Timestamp.from(Instant.now());
        updateDate = Timestamp.from(Instant.now());
    }

    @PreUpdate
    protected void prepareUpdateEntity() {
        updateDate = Timestamp.from(Instant.now());
    }
}
