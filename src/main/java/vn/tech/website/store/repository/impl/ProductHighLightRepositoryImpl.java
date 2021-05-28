package vn.tech.website.store.repository.impl;

import vn.tech.website.store.entity.EntityMapper;
import vn.tech.website.store.model.ProductHighLight;
import vn.tech.website.store.repository.ProductHighLightRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class ProductHighLightRepositoryImpl implements ProductHighLightRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ProductHighLight findLastRecord(Long productId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ph.product_highlight_id as productHighlightId, ph.product_id as productId, ph.date_add as dateAdd, ph.point as point from product_highlight ph where ph.product_id = :productId order by ph.date_add desc limit 1");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("productId", productId);
        return (ProductHighLight) EntityMapper.mapper(query, sb.toString(), ProductHighLight.class).get(0);
    }
}
