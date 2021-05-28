package vn.tech.website.store.repository;

import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.News;

public interface NewsRepository extends CrudRepository<News, Long>, NewsRepositoryCustom {
    News getByNewsId(Long newsId);
}
