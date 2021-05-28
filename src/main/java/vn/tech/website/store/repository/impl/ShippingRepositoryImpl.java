package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import vn.tech.website.store.dto.ShippingDto;
import vn.tech.website.store.dto.ShippingSearchDto;
import vn.tech.website.store.entity.EntityMapper;
import vn.tech.website.store.repository.ShippingRepositoryCustom;
import vn.tech.website.store.util.DbConstant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class ShippingRepositoryImpl implements ShippingRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List search(ShippingSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT "
                + "s.shipping_id AS shippingId, "
                + "s.name AS name, "
                + "s.code AS code, "
                + "s.price AS price, "
                + "s.detail AS detail, "
                + "s.path AS path, "
                + "s.status as status ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" GROUP BY s.shipping_id ");
        if (searchDto.getSortField() != null) {
            sb.append(" ORDER BY ");
            if (searchDto.getSortField().equals("name")) {
                sb.append(" s.name ");
            } else if (searchDto.getSortField().equals("code")) {
                sb.append(" s.code ");
            } else if (searchDto.getSortField().equals("price")) {
                sb.append(" s.price ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" ORDER BY s.shipping_id DESC ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        return EntityMapper.mapper(query, sb.toString(), ShippingDto.class);
    }

    @Override
    public Long countSearch(ShippingSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(s.shipping_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return Long.valueOf(query.getSingleResult().toString());
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, ShippingSearchDto searchDto) {
        sb.append(" FROM shipping s WHERE s.status = " + DbConstant.SHIPPING_STATUS_ACTIVE + " ");

        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            sb.append(" AND (s.name LIKE :keyword OR s.code LIKE :keyword OR s.price LIKE :keyword OR s.detail LIKE :keyword) ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, ShippingSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());
        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            query.setParameter("keyword", "%" + searchDto.getKeyword().trim() + "%");
        }
        return query;
    }
}
