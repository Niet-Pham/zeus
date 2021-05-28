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
@Table(name = "receive_notification")
public class ReceiveNotification extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receive_notification_id")
    private Long receiveNotificationId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "send_notification_id")
    private Long sendNotificationId;

    @Column(name = "status")
    private boolean status;

    @Column(name = "status_bell")
    private boolean statusBell;
}
