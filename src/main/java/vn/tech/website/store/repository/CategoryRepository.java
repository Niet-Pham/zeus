package vn.tech.website.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import vn.tech.website.store.model.Category;
import vn.tech.website.store.util.DbConstant;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long>, CategoryRepositoryCustom {

    @Query("select c from Category c " +
            " where c.status = " + DbConstant.CATEGORY_STATUS_ACTIVE
            + " and c.type = " + DbConstant.CATEGORY_TYPE_PRODUCT)
    List<Category> findAllCategoryProduct();

    @Query("select c from Category c " +
            "where c.categoryId <> :categoryId " +
            " and c.status = " + DbConstant.CATEGORY_STATUS_ACTIVE
            + " and c.type = " + DbConstant.CATEGORY_TYPE_PRODUCT)
    List<Category> findAllCategoryProductExpertId(@Param("categoryId") Long categoryId);

    @Query("select c from Category c " +
            " where c.status = " + DbConstant.CATEGORY_STATUS_ACTIVE
            + " and c.type = " + DbConstant.CATEGORY_TYPE_NEWS)
    List<Category> findAllCategoryNews();

    @Query("select c from Category c " +
            "where c.categoryId <> :categoryId " +
            " and c.status = " + DbConstant.CATEGORY_STATUS_ACTIVE
            + " and c.type = " + DbConstant.CATEGORY_TYPE_NEWS)
    List<Category> findAllCategoryNewsExpertId(@Param("categoryId") Long categoryId);

    @Query("select c from Category c where c.code = :code")
    Category getCateIdByCode(@Param("code") String code);
}
