package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import vn.tech.website.store.dto.CategoryDto;
import vn.tech.website.store.dto.CategorySearchDto;
import vn.tech.website.store.entity.EntityMapper;
import vn.tech.website.store.repository.CategoryRepositoryCustom;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List search(CategorySearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT "
                + "c.category_id AS categoryId, "
                + "c.code AS code, "
                + "c.name AS categoryName, "
                + "c.type AS type, "
                + "c.status AS status, "
                + "c.create_date AS createDate, "
                + "c.create_by AS createBy, "
                + "c.update_date AS updateDate, "
                + "c.update_by AS updateBy, "
                + "acc.full_name AS nameUpdate ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" GROUP BY c.category_id ");
        if (searchDto.getSortField() != null) {
            sb.append(" ORDER BY ");
            if (searchDto.getSortField().equals("code")) {
                sb.append(" c.code ");
            } else if (searchDto.getSortField().equals("categoryName")) {
                sb.append(" c.name collate utf8mb4_vietnamese_ci ");
            } else if (searchDto.getSortField().equals("status")) {
                sb.append(" c.status ");
            } else if (searchDto.getSortField().equals("updateDate")) {
                sb.append(" c.update_date ");
            } else if (searchDto.getSortField().equals("updateBy")) {
                sb.append(" name_update ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" ORDER BY c.category_id DESC ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        return EntityMapper.mapper(query, sb.toString(), CategoryDto.class);
    }

    @Override
    public Long countSearch(CategorySearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(c.category_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return Long.valueOf(query.getSingleResult().toString());
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, CategorySearchDto searchDto) {
        sb.append("FROM category c LEFT JOIN account acc ON c.update_by = acc.account_id WHERE 1 = 1 AND c.type = ").append(searchDto.getType());

        if (StringUtils.isNotBlank(searchDto.getCategoryName())) {
            sb.append(" AND (c.code LIKE :keyword OR c.name LIKE :keyword) ");
        }
        if (searchDto.getStatus() != null){
            sb.append(" AND c.status = :status ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, CategorySearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());
        if (StringUtils.isNotBlank(searchDto.getCategoryName())) {
            query.setParameter("keyword", "%" + searchDto.getKeyword().trim() + "%");
        }
        if (searchDto.getStatus() != null){
            query.setParameter("status", searchDto.getStatus());
        }
        return query;
    }
}
