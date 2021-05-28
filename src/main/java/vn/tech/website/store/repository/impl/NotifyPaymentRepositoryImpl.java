package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import vn.tech.website.store.dto.payment.NotifyPaymentDto;
import vn.tech.website.store.dto.payment.NotifyPaymentSearchDto;
import vn.tech.website.store.repository.NotifyPaymentRepositoryCustom;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/*
select np.notify_payment_id   as 'notify_payment_id',
       np.store_register_id as 'store_register_id',
       np.account_id          as 'account_id',
       np.asset_id            as 'asset_id',
       np.regulation_id       as 'regulation_id',
       np.amount              as 'amount',
       np.file_path           as 'file_path',
       np.file_name           as 'file_name',
       np.status              as 'status',
       np.create_date         as 'create_date',
       ar.code                as 'store_register_code',
       acc.full_name          as 'full_name',
       a.name                 as 'asset_name',
       r.code                 as 'regulation_code',
       p.payment_id           as 'payment_id'
from notify_payment np
         inner join store_register ar on np.store_register_id = ar.store_register_id
         inner join account acc on np.account_id = acc.account_id
         inner join asset a on np.asset_id = a.asset_id
         inner join regulation r on np.regulation_id = r.regulation_id
         left join payment p on ar.store_register_id = p.store_register_id
where 1 = 1
group by np.notify_payment_id, np.status
order by np.notify_payment_id desc, np.status asc
* */

public class NotifyPaymentRepositoryImpl implements NotifyPaymentRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<NotifyPaymentDto> search(NotifyPaymentSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("select np.notify_payment_id   as 'notify_payment_id', " +
                "       np.store_register_id as 'store_register_id', " +
                "       np.account_id          as 'account_id', " +
                "       np.asset_id            as 'asset_id', " +
                "       np.regulation_id       as 'regulation_id', " +
                "       np.amount              as 'amount', " +
                "       np.file_path           as 'file_path', " +
                "       np.file_name           as 'file_name', " +
                "       np.status              as 'status', " +
                "       np.create_date         as 'create_date', " +
                "       ar.code                as 'store_register_code', " +
                "       acc.is_org                as 'org', " +
                "       acc.full_name          as 'full_name', " +
                "       acc.org_name           as 'org_name', " +
                "       a.name                 as 'asset_name', " +
                "       r.code                 as 'regulation_code', " +
                "       p.payment_id           as 'payment_id', " +
                "       r.payment_start_time   as 'payment_start_time', " +
                "       r.payment_end_time     as 'payment_end_time' ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" group by np.notify_payment_id, np.status ");
        if (searchDto.getSortField() != null) {
            sb.append(" order by ");
            if (searchDto.getSortField().equals("fullNameNguoiDangKy")) {
                sb.append(" acc.full_name ");
            } else if (searchDto.getSortField().equals("amount")) {
                sb.append(" np.amount ");
            } else if (searchDto.getSortField().equals("assetName")) {
                sb.append(" a.name ");
            } else if (searchDto.getSortField().equals("regulationCode")) {
                sb.append(" r.code ");
            } else if (searchDto.getSortField().equals("status")) {
                sb.append(" np.status ");
            } else if (searchDto.getSortField().equals("paymentEndTime")) {
                    sb.append(" r.payment_end_time ");
            } else if (searchDto.getSortField().equals("createDate")) {
                sb.append(" np.create_date ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" order by np.notify_payment_id desc, np.status asc ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }
        List<NotifyPaymentDto> notifyPaymentDtoList = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        for (Object[] obj : result) {
            NotifyPaymentDto dto = new NotifyPaymentDto();
            dto.setNotifyPaymentId(ValueUtil.getLongByObject(obj[0]));
            dto.setStoreRegisterId(ValueUtil.getLongByObject(obj[1]));
            dto.setAccountId(ValueUtil.getLongByObject(obj[2]));
            dto.setAssetId(ValueUtil.getLongByObject(obj[3]));
            dto.setRegulationId(ValueUtil.getLongByObject(obj[4]));
            dto.setAmount(ValueUtil.getLongByObject(obj[5]));
            dto.setFilePath(ValueUtil.getStringByObject(obj[6]));
            dto.setFileName(ValueUtil.getStringByObject(obj[7]));
            dto.setStatus(ValueUtil.getIntegerByObject(obj[8]));
            dto.setCreateDate(ValueUtil.getDateByObject(obj[9]));
            dto.setStoreRegisterCode(ValueUtil.getStringByObject(obj[10]));

            if (!ValueUtil.getBooleanByObject(obj[11])) {
                dto.setFullNameNguoiDangKy(ValueUtil.getStringByObject(obj[12]));
            } else {
                dto.setFullNameNguoiDangKy(ValueUtil.getStringByObject(obj[13]));
            }

            dto.setAssetName(ValueUtil.getStringByObject(obj[14]));
            dto.setRegulationCode(ValueUtil.getStringByObject(obj[15]));
            dto.setPaymentId(ValueUtil.getLongByObject(obj[16]));
            dto.setPaymentStartTime(ValueUtil.getDateByObject(obj[17]));
            dto.setPaymentEndTime(ValueUtil.getDateByObject(obj[18]));
            notifyPaymentDtoList.add(dto);
        }
        return notifyPaymentDtoList;
    }

    @Override
    public Long countSearch(NotifyPaymentSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(np.notify_payment_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return Long.valueOf(query.getSingleResult().toString());
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, NotifyPaymentSearchDto searchDto) {
        sb.append(" from notify_payment np " +
                "         inner join store_register ar on np.store_register_id = ar.store_register_id " +
                "         inner join account acc on np.account_id = acc.account_id " +
                "         inner join asset a on np.asset_id = a.asset_id " +
                "         inner join regulation r on np.regulation_id = r.regulation_id " +
                "         left join payment p on ar.store_register_id = p.store_register_id " +
                " where 1 = 1 ");
        if (searchDto.getStoreRegisterId() != null) {
            sb.append(" and np.store_register_id = :storeRegisterId ");
        }
        if (searchDto.getAccountId() != null) {
            sb.append(" and np.account_id = :accountId ");
        }
        if (searchDto.getAssetId() != null) {
            sb.append(" and np.asset_id = :assetId ");
        }
        if (StringUtils.isNotBlank(searchDto.getAssetName())) {
            sb.append(" and a.name like :assetName ");
        }
        if (searchDto.getRegulationId() != null) {
            sb.append(" and np.regulation_id = :regulationId ");
        }
        if (searchDto.getStatus() != null) {
            sb.append(" and np.status = :status ");
        }
        if (searchDto.getFromDate() != null) {
            sb.append(" and np.create_date >= :fromDate ");
        }
        if (searchDto.getToDate() != null) {
            sb.append(" and np.create_date <= :toDate ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, NotifyPaymentSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());
        if (searchDto.getStoreRegisterId() != null) {
            query.setParameter("storeRegisterId", searchDto.getStoreRegisterId());
        }
        if (searchDto.getAccountId() != null) {
            query.setParameter("accountId", searchDto.getAccountId());
        }
        if (searchDto.getAssetId() != null) {
            query.setParameter("assetId", searchDto.getAssetId());
        }
        if (StringUtils.isNotBlank(searchDto.getAssetName())) {
            query.setParameter("assetName", "%" + searchDto.getAssetName().trim() + "%");
        }
        if (searchDto.getRegulationId() != null) {
            query.setParameter("regulationId", searchDto.getRegulationId());
        }
        if (searchDto.getStatus() != null) {
            query.setParameter("status", searchDto.getStatus());
        }
        if (searchDto.getFromDate() != null) {
            query.setParameter("fromDate",  searchDto.getFromDate());
        }
        if (searchDto.getToDate() != null) {
            query.setParameter("toDate",  searchDto.getToDate());
        }
        return query;
    }
}
