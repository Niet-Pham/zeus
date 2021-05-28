package vn.tech.website.store.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import vn.tech.website.store.dto.base.RoleSearchDto;
import vn.tech.website.store.entity.EntityMapper;
import vn.tech.website.store.model.Role;
import vn.tech.website.store.repository.RoleRepositoryCustom;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List search(RoleSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT r.role_id AS roleId, r.name AS name, r.status AS status, r.can_modify AS canModify ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        sb.append(" GROUP BY r.role_id ");
        if (searchDto.getSortField() != null) {
            sb.append(" ORDER BY ");
            if (searchDto.getSortField().equals("name")) {
                sb.append(" r.name collate utf8mb4_vietnamese_ci ");
            } else if (searchDto.getSortField().equals("status")) {
                sb.append(" r.status ");
            }
            sb.append(searchDto.getSortOrder());
        } else {
            sb.append(" ORDER BY r.role_id DESC ");
        }

        Query query = createQueryObjForSearch(sb, searchDto);
        if (searchDto.getPageSize() > 0) {
            query.setFirstResult(searchDto.getPageIndex());
            query.setMaxResults(searchDto.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(Integer.MAX_VALUE);
        }

        return EntityMapper.mapper(query, sb.toString(), Role.class);
    }

    @Override
    public Long countSearch(RoleSearchDto searchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(r.role_id) ");
        appendQueryFromAndWhereForSearch(sb, searchDto);
        Query query = createQueryObjForSearch(sb, searchDto);
        return Long.valueOf(query.getSingleResult().toString());
    }

    private void appendQueryFromAndWhereForSearch(StringBuilder sb, RoleSearchDto searchDto) {
        sb.append(" FROM role r ");
        sb.append(" WHERE 1 = 1 ");

        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            sb.append(" AND r.name LIKE :keyword) ");
        }
    }

    private Query createQueryObjForSearch(StringBuilder sb, RoleSearchDto searchDto) {
        Query query = entityManager.createNativeQuery(sb.toString());
        if (StringUtils.isNotBlank(searchDto.getKeyword())) {
            query.setParameter("keyword", "%" + searchDto.getKeyword().trim() + "%");
        }
        return query;
    }
}
