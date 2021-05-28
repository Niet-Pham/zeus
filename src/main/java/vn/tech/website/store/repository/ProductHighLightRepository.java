package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.ProductHighLight;

public interface ProductHighLightRepository extends CrudRepository<ProductHighLight, Long>, ProductHighLightRepositoryCustom {
}
