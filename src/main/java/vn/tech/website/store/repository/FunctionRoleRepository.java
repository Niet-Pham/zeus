package vn.tech.website.store.repository;

import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.FunctionRole;

import javax.transaction.Transactional;

public interface FunctionRoleRepository extends CrudRepository<FunctionRole, Long>, FunctionRoleRepositoryCustom {
    @Transactional
    void deleteByRoleId(Integer roleId);
}
