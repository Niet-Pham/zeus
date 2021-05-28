package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.tech.website.store.model.Brand;
import vn.tech.website.store.util.DbConstant;

import java.util.List;

public interface BrandRepository extends CrudRepository<Brand, Long>, BrandRepositoryCustom {

    @Query("select b from Brand b where b.status = " + DbConstant.BRAND_STATUS_ACTIVE)
    List<Brand> findAll();

    @Query("select b from Brand b where b.brandId <> :brandId and b.status = "+ DbConstant.BRAND_STATUS_ACTIVE)
    List<Brand> findAllExpertId(@Param("brandId") Long brandId);
}
