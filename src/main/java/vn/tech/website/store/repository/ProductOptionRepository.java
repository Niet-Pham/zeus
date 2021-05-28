package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.tech.website.store.model.ProductOption;
import vn.tech.website.store.util.DbConstant;

import java.util.List;

public interface ProductOptionRepository extends CrudRepository<ProductOption, Long>, ProductOptionRepositoryCustom {

    @Query("select po from ProductOption po where po.status = " + DbConstant.OPTION_STATUS_ACTIVE)
    List<ProductOption> findAll();

    @Query("select po from ProductOption po where po.productOptionId <> :productOptionId and po.status = " + DbConstant.OPTION_STATUS_ACTIVE)
    List<ProductOption> findAllExpertId(@Param("productOptionId") Long productOptionId);

    ProductOption getByProductOptionId(Long productOptionId);
}
