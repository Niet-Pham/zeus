package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.tech.website.store.model.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long>, ProductRepositoryCustom {
    @Query("select p from Product p where p.type = :type and p.productId <> :productId")
    List<Product> getAllByTypeAndExpertId(@Param("type") Integer type, @Param("productId") Long productId);

    List<Product> getAllByType(Integer type);

    @Query("select p from Product p where p.categoryId = :categoryId and p.type <> :type")
    List<Product> getByCategoryIdExpertType(@Param("categoryId") Long cateId, @Param("type") Integer type);

    @Query("select max(p.countCode) from Product p")
    Long findMaxCountCode();

    List<Product> findByCode(String code);

    Product getByProductId(Long productId);
}
