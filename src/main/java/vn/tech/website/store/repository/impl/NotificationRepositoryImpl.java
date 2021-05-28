package vn.tech.website.store.repository.impl;

import vn.tech.website.store.dto.NotificationSearchDto;
import vn.tech.website.store.dto.NotificationDto;
import vn.tech.website.store.repository.NotificationRepositoryCustom;
import vn.tech.website.store.util.DateUtil;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationRepositoryImpl implements NotificationRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NotificationDto> search(NotificationSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("select sn.send_notification_id    as 'send_notification_id', " +
                "       sn.account_id              as 'send_account_id', " +
                "       accs.full_name             as 'send_fullname', " +
                "       sn.content                 as 'send_content_id', " +
                "       sn.status                  as 'send_status', " +
                "       sn.create_date             as 'send_create_date', " +
                "       sn.create_by               as 'send_create_by', " +
                "       rn.receive_notification_id as 'receive_notification_id', " +
                "       rn.account_id              as 'receive_account_id', " +
                "       accr.full_name             as 'receive_fullname', " +
                "       rn.status                  as 'receive_status', " +
                "       rn.create_date             as 'receive_create_date', " +
                "       rn.create_by               as 'receive_create_by', " +
                "       rn.status_bell             as 'receive_status_bell'," +
                "       sn.object_id               as 'object_id',  " +
                "       sn.type                    as 'type' ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" order by rn.receive_notification_id desc ");

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        List<NotificationDto> notificationDtoList = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        for (Object[] obj : result) {
            NotificationDto dto = new NotificationDto();
            dto.setSendNotificationId(ValueUtil.getLongByObject(obj[0]));
            dto.setSendAccountId(ValueUtil.getLongByObject(obj[1]));
            dto.setSendFullname(ValueUtil.getStringByObject(obj[2]));
            dto.setSendContent(ValueUtil.getStringByObject(obj[3]));
            dto.setSendStatus(ValueUtil.getIntegerByObject(obj[4]));
            dto.setSendCreateDate(ValueUtil.getDateByObject(obj[5]));
            dto.setSendCreateBy(ValueUtil.getLongByObject(obj[6]));
            dto.setReceiveNotificationId(ValueUtil.getLongByObject(obj[7]));
            dto.setAccountId(ValueUtil.getLongByObject(obj[8]));
            dto.setReceiveFullname(ValueUtil.getStringByObject(obj[9]));
            dto.setStatus(ValueUtil.getBooleanByObject(obj[10]));
            Date dateBeforeNow = ValueUtil.getDateByObject(obj[11]);
            dto.setCreateDate(dateBeforeNow);
            dto.setDateBeforeNow(dateBeforeNow != null ? DateUtil.countTimeBefore(dateBeforeNow) + " " + DateUtil.typeTime + " trước" : "");
            dto.setCreateBy(ValueUtil.getLongByObject(obj[12]));
            dto.setStatusBell(ValueUtil.getBooleanByObject(obj[13]));
            dto.setObjectId(ValueUtil.getLongByObject(obj[14]));
            dto.setType(ValueUtil.getIntegerByObject(obj[15]));
            notificationDtoList.add(dto);
        }
        return notificationDtoList;
    }

    @Override
    public Long countRecordNotSeen(NotificationSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(rn.receive_notification_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return Long.valueOf(query.getSingleResult().toString());
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, NotificationSearchDto searchDto) {
        sb.append(" from receive_notification rn " +
                "         inner join send_notification sn on sn.send_notification_id = rn.send_notification_id " +
                "         left join account accr on rn.account_id = accr.account_id " +
                "         left join account accs on sn.account_id = accs.account_id " +
                " where 1 = 1 ");

        if (searchDto.getAccountId() != null) {
            sb.append(" and rn.account_id = :accountId ");
        }
        if (!searchDto.isCheck()) {
            sb.append(" and rn.status_bell = :statusBell ");
        }
        if (searchDto.getReceiveNotificationLastId() != null) {
            sb.append(" and rn.receive_notification_id < :receiveNotificationLastId ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, NotificationSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());

        if (searchDto.getAccountId() != null) {
            query.setParameter("accountId", searchDto.getAccountId());
        }
        if (!searchDto.isCheck()) {
            query.setParameter("statusBell", DbConstant.NOTIFICATION_STATUS_BELL_NOT_SEEN);
        }
        if (searchDto.getReceiveNotificationLastId() != null) {
            query.setParameter("receiveNotificationLastId", searchDto.getReceiveNotificationLastId());
        }

        return query;
    }
}
