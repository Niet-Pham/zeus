package vn.tech.website.store.service;

import vn.tech.website.store.dto.base.FunctionDto;
import vn.tech.website.store.model.Role;

import java.util.List;

public interface RoleService {
    void save(Role roleId, List<FunctionDto> functionRoleList);
    void delete(Integer roleId);
}
