package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import vn.tech.website.store.dto.CategoryDto;
import vn.tech.website.store.dto.CategorySearchDto;
import vn.tech.website.store.dto.NewsDto;
import vn.tech.website.store.dto.NewsSearchDto;
import vn.tech.website.store.entity.EntityMapper;
import vn.tech.website.store.repository.NewsRepositoryCustom;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.DbConstant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class NewsRepositoryImpl implements NewsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List search(NewsSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT "
                + "n.news_id AS newsId, "
                + "n.category_id AS categoryId, "
                + "n.title AS title, "
                + "c.name AS categoryName, "
                + "n.short_content AS shortContent, "
                + "n.content AS content, "
                + "n.status AS status, "
                + "n.img_path AS imgPath, "
                + "n.create_date AS createDate, "
                + "n.create_by AS createBy, "
                + "n.update_date AS updateDate, "
                + "n.update_by AS updateBy,"
                + "acc.full_name as nameUpdate ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" GROUP BY n.news_id ");
        if (searchDto.getSortField() != null) {
            sb.append(" ORDER BY ");
            if (searchDto.getSortField().equals("code")) {
                sb.append(" n.code ");
            } else if (searchDto.getSortField().equals("title")) {
                sb.append(" n.title collate utf8mb4_vietnamese_ci ");
            } else if (searchDto.getSortField().equals("shortContent")) {
                sb.append(" n.short_content ");
            } else if (searchDto.getSortField().equals("updateDate")) {
                sb.append(" n.update_date ");
            } else if (searchDto.getSortField().equals("nameUpdate")) {
                sb.append(" name_update ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" ORDER BY n.news_id DESC ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        return EntityMapper.mapper(query, sb.toString(), NewsDto.class);
    }

    @Override
    public Long countSearch(NewsSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(n.news_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return Long.valueOf(query.getSingleResult().toString());
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, NewsSearchDto searchDto) {
        sb.append(" FROM news n " +
                " INNER JOIN category c on n.category_id = c.category_id " +
                " LEFT JOIN account acc on n.update_by = acc.account_id ");
        sb.append(" WHERE 1 = 1 AND n.status = " + DbConstant.NEWS_STATUS_ACTIVE + " ");

        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            sb.append(" AND (n.title LIKE :keyword OR n.short_content LIKE :keyword) ");
        }
        if (searchDto.getCategoryId() != null){
            sb.append(" AND n.category_id = :categoryId ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, NewsSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());
        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            query.setParameter("keyword", "%" + searchDto.getKeyword().trim() + "%");
        }
        if (searchDto.getCategoryId() != null){
            query.setParameter("categoryId", searchDto.getCategoryId());
        }
        return query;
    }
}
