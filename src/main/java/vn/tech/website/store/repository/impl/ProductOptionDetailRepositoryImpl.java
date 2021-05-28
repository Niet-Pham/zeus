package vn.tech.website.store.repository.impl;

import org.springframework.stereotype.Repository;
import vn.tech.website.store.dto.ProductOptionDetailDto;
import vn.tech.website.store.dto.ProductOptionDetailSearchDto;
import vn.tech.website.store.entity.EntityMapper;
import vn.tech.website.store.repository.ProductOptionDetailRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ProductOptionDetailRepositoryImpl implements ProductOptionDetailRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List searchOption(ProductOptionDetailSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT pod.product_id AS 'productId', pod.product_option_id AS 'productOptionId', po.name AS 'productOptionName', po.type AS 'productOptionType' " +
                "FROM product_option_detail pod " +
                "         INNER JOIN product_option po ON pod.product_option_id = po.product_option_id " +
                "WHERE 1=1 ");
        if (searchDto.getParentId() != null){
            sb.append(" AND pod.product_id IN (SELECT pl.child_id FROM product_link pl WHERE pl.parent_id = :parentId) ");
        }
        if (searchDto.getParentId() == null){
            sb.append(" AND pod.product_id = :productId ");
        }
         sb.append("GROUP BY pod.product_id, po.type, po.name, pod.product_option_id");
        Query query = entityManager.createNativeQuery(sb.toString());
        if (searchDto.getParentId() != null ) {
            query.setParameter("parentId", searchDto.getParentId());
        }
        if (searchDto.getParentId() == null){
            query.setParameter("productId", searchDto.getProductIdTypeNone());
        }
        return EntityMapper.mapper(query, sb.toString(), ProductOptionDetailDto.class);
    }
}
