package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import vn.tech.website.store.dto.BrandDto;
import vn.tech.website.store.dto.BrandSearchDto;
import vn.tech.website.store.repository.BrandRepositoryCustom;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class BrandRepositoryImpl implements BrandRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BrandDto> search(BrandSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT "
                + "b.brand_id, "
                + "b.name, "
                + "b.code, "
                + "b.status, "
                + "b.create_date, "
                + "b.create_by, "
                + "b.update_date, "
                + "acc.full_name as name_update ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" GROUP BY b.brand_id ");
        if (searchDto.getSortField() != null) {
            sb.append(" ORDER BY ");
            if (searchDto.getSortField().equals("brandId")) {
                sb.append(" b.brand_id ");
            } else if (searchDto.getSortField().equals("brandName")) {
                sb.append(" b.name collate utf8mb4_vietnamese_ci ");
            } else if (searchDto.getSortField().equals("code")) {
                sb.append(" b.code  ");
            } else if (searchDto.getSortField().equals("status")) {
                sb.append(" b.status ");
            }  else if (searchDto.getSortField().equals("updateDate")) {
                sb.append(" b.update_date ");
            } else if (searchDto.getSortField().equals("updateBy")) {
                sb.append(" name_update ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" ORDER BY b.brand_id DESC ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        List<BrandDto> dtoList = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        for (Object[] obj : result) {
            BrandDto dto = new BrandDto();
            dto.setBrandId(ValueUtil.getLongByObject(obj[0]));
            dto.setBrandName(ValueUtil.getStringByObject(obj[1]));
            dto.setCode(ValueUtil.getStringByObject(obj[2]));
            dto.setStatus(ValueUtil.getIntegerByObject(obj[3]));
            dto.setCreateDate(ValueUtil.getDateByObject(obj[4]));
            dto.setCreateBy(ValueUtil.getLongByObject(obj[5]));
            dto.setUpdateDate(ValueUtil.getDateByObject(obj[6]));
            dto.setNameUpdate(ValueUtil.getStringByObject(obj[7]));
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public Long countSearch(BrandSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(b.brand_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return Long.valueOf(query.getSingleResult().toString());
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, BrandSearchDto searchDto) {
        sb.append("FROM brand b " +
                " LEFT JOIN account acc ON b.update_by = acc.account_id ");
        sb.append(" WHERE 1=1 AND b.status = " + DbConstant.BRAND_STATUS_ACTIVE);

        if (StringUtils.isNotBlank(searchDto.getBrandName())) {
            sb.append(" AND b.name LIKE :brandName ");
        }
        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            sb.append(" AND (b.name LIKE :keyword OR b.code LIKE :keyword) ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, BrandSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());
        if (StringUtils.isNotBlank(searchDto.getBrandName())) {
            query.setParameter("brandName", "%" + searchDto.getBrandName().trim() + "%");
        }
        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            query.setParameter("keyword", "%" + searchDto.getKeyword().trim() + "%");
        }

        return query;
    }
}
