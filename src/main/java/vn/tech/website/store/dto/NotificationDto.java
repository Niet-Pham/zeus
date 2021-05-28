package vn.tech.website.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.ReceiveNotification;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    @JsonProperty("receive_notification_id")
    private Long receiveNotificationId;
    @JsonProperty("account_id")
    private Long accountId;
    @JsonProperty("send_notification_id")
    private Long sendNotificationId;
    @JsonProperty("status")
    private boolean status;
    @JsonProperty("status_bell")
    private boolean statusBell;
    @JsonProperty("create_date")
    private Date createDate;
    @JsonProperty("create_by")
    private Long createBy;
    @JsonProperty("send_account_id")
    private Long sendAccountId;
    @JsonProperty("send_content")
    private String sendContent;
    @JsonProperty("send_status")
    private Integer sendStatus;
    @JsonProperty("send_fullname")
    private String sendFullname;
    @JsonProperty("send_create_date")
    private Date sendCreateDate;
    @JsonProperty("send_create_by")
    private Long sendCreateBy;
    @JsonProperty("receive_fullname")
    private String receiveFullname;
    @JsonProperty("date_before_now")
    private String dateBeforeNow;
    @JsonProperty("object_id")
    private Long objectId;
    @JsonProperty("type")
    private Integer type;
}
