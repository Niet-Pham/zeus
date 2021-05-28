package vn.tech.website.store.repository;

import vn.tech.website.store.model.ProductHighLight;

public interface ProductHighLightRepositoryCustom {
    ProductHighLight findLastRecord(Long productId);
}
