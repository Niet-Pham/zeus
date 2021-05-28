package vn.tech.website.store.controller.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.controller.frontend.BaseFEController;
import vn.tech.website.store.dto.NotificationDto;
import vn.tech.website.store.dto.NotificationSearchDto;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.ReceiveNotification;
import vn.tech.website.store.model.SendNotification;
import vn.tech.website.store.repository.NotificationRepository;
import vn.tech.website.store.repository.SendNotificationRepository;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.FacesUtil;
import vn.tech.website.store.util.StringUtil;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Named
@Scope(value = "session")
public class NotificationController extends BaseController {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private SendNotificationRepository sendNotificationRepository;

    private List<NotificationDto> notificationDtoList;
    private NotificationSearchDto searchDto;
    private Long countRecord;
    private String notificationJson;
    private Long notificationLastId;

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            resetAll();
        }
    }

    public void resetAll() {
        searchDto = new NotificationSearchDto();
        searchDto.setAccountId(getAuthorizationController().getAccountDto().getAccountId());
        searchDto.setPageSize(Constant.NOTIFICATION_PAGE_SIZE);
        searchDto.setCheck(true);
        notificationDtoList = notificationRepository.search(searchDto);
        notificationLastId = !notificationDtoList.isEmpty() ? notificationDtoList.get(notificationDtoList.size() - 1).getReceiveNotificationId() : null;
        searchDto.setCheck(false);
        countRecord = notificationRepository.countRecordNotSeen(searchDto);
        notificationJson = StringUtil.toJson(notificationDtoList);
    }

    public void seenAllNotification() {
        notificationRepository.changeStatusBell(getAuthorizationController().getAccountDto().getAccountId(), DbConstant.NOTIFICATION_STATUS_BELL_SEEN);
        resetAll();
    }

    public void redirectPage() {
        Map<String, String> params = FacesUtil.getRequestParameterMap();
        Long notificationId = Long.valueOf(params.get("notificationId"));
        ReceiveNotification receiveNotification = notificationRepository.findById(notificationId).orElse(null);
        receiveNotification.setStatus(DbConstant.NOTIFICATION_STATUS_SEEN);
        notificationRepository.save(receiveNotification);
        SendNotification sendNotification = sendNotificationRepository.getBySendNotificationId(receiveNotification.getSendNotificationId());
        if (sendNotification.getType() == DbConstant.NOTIFICATION_TYPE_ORDER){
            FacesUtil.redirect("/admin/order-manage/order.xhtml?orderid="+sendNotification.getObjectId());
        }
        if (sendNotification.getType() == DbConstant.NOTIFICATION_TYPE_PRODUCT){
            FacesUtil.redirect("/admin/product-manage/product.xhtml?proid="+sendNotification.getObjectId());
        }
        if (sendNotification.getType() == DbConstant.NOTIFICATION_TYPE_NEWS){
            FacesUtil.redirect("/admin/news-manage/news.xhtml?newsid="+sendNotification.getObjectId());
        }
        resetAll();
    }

    public void loadMoreAds() {
        searchDto.setCheck(true);
        searchDto.setReceiveNotificationLastId(notificationLastId);
        List<NotificationDto> notificationDtoListLoadMore = notificationRepository.search(searchDto);
        if (!notificationDtoListLoadMore.isEmpty()) {
            notificationLastId = notificationDtoListLoadMore.get(notificationDtoListLoadMore.size() - 1).getReceiveNotificationId();
        }
        notificationJson = StringUtil.toJson(notificationDtoListLoadMore);
    }

    @Override
    protected EScope getMenuId() {
        return null;
    }
}
