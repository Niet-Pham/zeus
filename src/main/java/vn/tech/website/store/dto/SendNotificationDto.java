package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.tech.website.store.model.SendNotification;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationDto extends SendNotification {
    private String accountName;}
