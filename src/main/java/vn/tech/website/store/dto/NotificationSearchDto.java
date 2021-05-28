package vn.tech.website.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSearchDto extends BaseSearchDto {
    private Long accountId;
    private Long receiveNotificationLastId;
    private boolean check;
    private Long receiveNotificationId;
}
