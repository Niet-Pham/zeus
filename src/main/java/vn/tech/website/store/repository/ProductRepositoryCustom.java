package vn.tech.website.store.repository;

import org.springframework.data.repository.query.Param;
import vn.tech.website.store.dto.ProductDto;
import vn.tech.website.store.dto.ProductSearchDto;
import vn.tech.website.store.model.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductDto> search(ProductSearchDto searchDto);

    Long countSearch(ProductSearchDto searchDto);

    List<Product> getAllExpertType(Long id, Integer type, Integer limit);

    List<Product> searchByKeyword(String keyword);

    List<ProductDto> searchProductHighlight();
}
