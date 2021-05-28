package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.tech.website.store.model.ProductImage;

import java.util.List;
import java.util.Set;

public interface ProductImageRepository extends CrudRepository<ProductImage, Long> {
    List<ProductImage> getByProductId(Long productId);

    @Query("select pi.imagePath from ProductImage pi where pi.productId = :productId")
    Set<String> getImagePathByProductId(@Param("productId")Long productId);

    List<ProductImage> findAllByProductId(Long productId);
}
