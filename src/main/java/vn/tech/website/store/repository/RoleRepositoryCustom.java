package vn.tech.website.store.repository;

import vn.tech.website.store.dto.base.RoleSearchDto;
import vn.tech.website.store.model.Role;

import java.util.List;

public interface RoleRepositoryCustom {
    List<Role> search(RoleSearchDto roleSearchDto);
    Long countSearch(RoleSearchDto roleSearchDto);
}
