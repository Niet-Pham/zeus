package vn.tech.website.store.repository;

import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.Function;

public interface FunctionRepository extends CrudRepository<Function, Long>, FunctionRepositoryCustom {

}
