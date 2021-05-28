package vn.tech.website.store.repository;

import org.springframework.data.repository.CrudRepository;
import vn.tech.website.store.model.Role;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Integer>, RoleRepositoryCustom {
    Role findRoleByRoleId(Integer roleId);
}
