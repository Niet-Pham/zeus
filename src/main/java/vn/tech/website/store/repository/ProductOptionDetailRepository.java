package vn.tech.website.store.repository;

import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.ProductOptionDetail;

import java.util.List;

public interface ProductOptionDetailRepository extends CrudRepository<ProductOptionDetail, Long>, ProductOptionDetailRepositoryCustom {
    List<ProductOptionDetail> findAllByProductId(Long productId);
}
