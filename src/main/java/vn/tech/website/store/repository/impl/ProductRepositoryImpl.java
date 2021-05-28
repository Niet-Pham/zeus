package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import vn.tech.website.store.dto.*;
import vn.tech.website.store.entity.EntityMapper;
import vn.tech.website.store.model.Product;
import vn.tech.website.store.repository.ProductImageRepository;
import vn.tech.website.store.repository.ProductRepositoryCustom;
import vn.tech.website.store.util.Constant;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List search(ProductSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT "
                + "p.product_id AS productId, "
                + "p.name AS productName, "
                + "p.brand_id AS brandId, "
                + "b.name AS brandName, "
                + "p.category_id AS categoryId, "
                + "c.name AS categoryName, "
                + "p.type AS type, "
                + "p.code AS code, "
                + "p.count_code AS countCode, "
                + "p.description AS description, "
                + "p.quantity AS quantity, "
                + "p.price AS price, "
                + "p.discount AS discount, "
                + "p.status AS status, "
                + "p.create_date AS createDate, "
                + "p.create_by AS createBy, "
                + "p.update_date AS updateDate, "
                + "p.update_by AS updateBy, "
                + "p.price - p.price * p.discount / 100 AS priceAfterDiscount, "
                + "(SELECT pi.image_path FROM product_image pi WHERE pi.product_id = p.product_id LIMIT 1) AS imageToShow, "
                + "(SELECT sum(p_v.quantity) FROM product_link pl INNER JOIN product p_v ON pl.child_id = p_v.product_id WHERE pl.parent_id = p.product_id) AS totalQtyParent ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" GROUP BY p.product_id ");
        if (searchDto.getSortField() != null) {
            sb.append(" ORDER BY ");
            if (searchDto.getSortField().equals("productId")) {
                sb.append(" p.product_id ");
            } else if (searchDto.getSortField().equals("productName")) {
                sb.append(" p.name collate utf8mb4_vietnamese_ci ");
            } else if (searchDto.getSortField().equals("status")) {
                sb.append(" p.status ");
            } else if (searchDto.getSortField().equals("type")) {
                sb.append(" p.type ");
            } else if (searchDto.getSortField().equals("code")) {
                sb.append(" p.code ");
            } else if (searchDto.getSortField().equals("updateDate")) {
                sb.append(" p.update_date ");
            } else if (searchDto.getSortField().equals("updateBy")) {
                sb.append(" p.updateBy ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" ORDER BY p.product_id DESC ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        return EntityMapper.mapper(query, sb.toString(), ProductDto.class);
    }

    @Override
    public Long countSearch(ProductSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(p.product_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return Long.valueOf(query.getSingleResult().toString());
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, ProductSearchDto searchDto) {
        sb.append("FROM product p " +
                " INNER JOIN category c ON p.category_id = c.category_id " +
                " INNER JOIN brand b ON p.brand_id = b.brand_id ");
        if (searchDto.getParentId() != null) {
            sb.append(" INNER JOIN product_link pl ON p.product_id = pl.child_id ");
        }
        if (searchDto.getOptionCondition() != null) {
            sb.append(" LEFT JOIN product_option_detail pod ON p.product_id = pod.product_id ");
        }
        sb.append(" WHERE 1 = 1 ");
        sb.append(" AND p.status = " + DbConstant.PRODUCT_STATUS_ACTIVE);

        if (StringUtils.isNotBlank(searchDto.getProductName())) {
            sb.append(" AND p.name LIKE :productName ");
        }
        if (searchDto.getCategoryId() != null) {
            sb.append(" AND p.category_id = :categoryId ");
        }
        if (searchDto.getBrandId() != null) {
            sb.append(" AND p.brand_id = :brandId ");
        }
        if (StringUtils.isNotBlank(searchDto.getCode())) {
            sb.append(" AND p.code LIKE :code ");
        }
        if (searchDto.getType() != null) {
            sb.append(" AND p.type = :type ");
        }
        if (searchDto.getExpertType() != null) {
            sb.append(" AND p.type <> :expertType ");
        }

        if (searchDto.getProductId() != null) {
            sb.append(" AND p.product_id = :productId ");
        }
        if (searchDto.getParentId() != null) {
            sb.append(" AND pl.parent_id = :parentId ");
        }

        if (searchDto.isEnableSearchPrice()) {
            sb.append(" AND :minPrice <= p.price ");
            if (searchDto.getMaxPrice() != null) {
                sb.append(" AND p.price <= :maxPrice ");
            }
        }

        if (searchDto.getOptionCondition() != null) {
            sb.append(" AND pod.product_option_id IN ").append(searchDto.getOptionCondition()).append(" ");
        }

        if (searchDto.getKeyword() != null) {
            sb.append(" AND (p.name LIKE :keyword OR p.code LIKE :keyword) ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, ProductSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());
        if (StringUtils.isNotBlank(searchDto.getProductName())) {
            query.setParameter("productName", "%" + searchDto.getProductName().trim() + "%");
        }
        if (searchDto.getCategoryId() != null) {
            query.setParameter("categoryId", searchDto.getCategoryId());
        }
        if (searchDto.getBrandId() != null) {
            query.setParameter("brandId", searchDto.getBrandId());
        }
        if (StringUtils.isNotBlank(searchDto.getCode())) {
            query.setParameter("code", "%" + searchDto.getCode().trim() + "%");
        }
        if (searchDto.getType() != null) {
            query.setParameter("type", searchDto.getType());
        }
        if (searchDto.getExpertType() != null) {
            query.setParameter("expertType", searchDto.getExpertType());
        }

        if (searchDto.getProductId() != null) {
            query.setParameter("productId", searchDto.getProductId());
        }
        if (searchDto.getParentId() != null) {
            query.setParameter("parentId", searchDto.getParentId());
        }

        if (searchDto.isEnableSearchPrice()) {
            query.setParameter("minPrice", searchDto.getMinPrice());
            if (searchDto.getMaxPrice() != null) {
                query.setParameter("maxPrice", searchDto.getMaxPrice());
            }
        }

        if (searchDto.getKeyword() != null) {
            query.setParameter("keyword", "%" + searchDto.getKeyword() + "%");
        }

        return query;
    }

    @Override
    public List getAllExpertType(Long id, Integer type, Integer limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("select p.product_id  as productId," +
                "       p.brand_id    as brandId," +
                "       p.category_id as categoryId," +
                "       p.name        as productName," +
                "       p.code        as code," +
                "       p.count_code  as countCode," +
                "       p.quantity    as quantity," +
                "       p.type        as type," +
                "       p.description as description," +
                "       p.price       as price," +
                "       p.discount    as discount," +
                "       p.status      as status," +
                "       p.create_date as createDate," +
                "       p.create_by   as createBy," +
                "       p.update_date as updateDate," +
                "       p.update_by   as updateBy " +
                "from product p " +
                "where p.product_id > :idSrc " +
                "  and p.type <> :typePro " +
                "limit :limitLoad");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idSrc", id);
        query.setParameter("typePro", type);
        query.setParameter("limitLoad", limit);
        return EntityMapper.mapper(query, sb.toString(), Product.class);
    }

    @Override
    public List searchByKeyword(String keyword) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT p.product_id AS productId, p.name AS productName " +
                "FROM product p " +
                "WHERE p.type = :type " +
                "  AND p.status = :status " +
                "  AND (p.name LIKE :keyword OR p.code LIKE :keyword) LIMIT :limit");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("type", DbConstant.PRODUCT_TYPE_CHILD);
        query.setParameter("status", DbConstant.PRODUCT_STATUS_ACTIVE);
        query.setParameter("keyword", "%" + keyword + "%");
        query.setParameter("limit", Constant.PAGE_SIZE_MAX);
        return EntityMapper.mapper(query, sb.toString(), Product.class);
    }

    @Override
    public List searchProductHighlight() {
        StringBuilder sb = new StringBuilder();
        sb.append("select "
                + " p.product_id AS productId, "
                + "p.name AS productName, "
                + "p.type AS type, "
                + "p.code AS code, "
                + "p.count_code AS countCode, "
                + "p.description AS description, "
                + "p.quantity AS quantity, "
                + "p.price AS price, "
                + "p.discount AS discount, "
                + "p.price - p.price * p.discount / 100 AS priceAfterDiscount, "
                + "(SELECT pi.image_path FROM product_image pi WHERE pi.product_id = p.product_id LIMIT 1) AS imageToShow, "
                + "(SELECT sum(p_v.quantity) FROM product_link pl INNER JOIN product p_v ON pl.child_id = p_v.product_id WHERE pl.parent_id = p.product_id) AS totalQtyParent "
                + " from product p "
                + " where p.product_id in (select ph.product_id "
                + " from product_highlight ph "
                + " where month(ph.date_add) = month(now()) "
                + " order by ph.point desc) limit 6");
        Query query = entityManager.createNativeQuery(sb.toString());
        return EntityMapper.mapper(query, sb.toString(), ProductDto.class);
    }
}
