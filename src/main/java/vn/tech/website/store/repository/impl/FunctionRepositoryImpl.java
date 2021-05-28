package vn.tech.website.store.repository.impl;

import org.springframework.stereotype.Repository;
import vn.tech.website.store.dto.base.FunctionDto;
import vn.tech.website.store.entity.EAction;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.repository.FunctionRepositoryCustom;
import vn.tech.website.store.util.ValueUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class FunctionRepositoryImpl implements FunctionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FunctionDto> findFunctionsByRoleId(Integer roleId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT fr.function_role_id, "
                + "f.function_id,"
                + "f.scope, "
                + "f.action, "
                + "f.name "
                + "FROM function_role fr "
                +   "INNER JOIN function f "
                +   "ON f.function_id = fr.function_id "
                + "WHERE fr.role_id = :roleId");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("roleId", roleId);

        List<FunctionDto> functionDtoList = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        for (Object[] obj : result) {
            FunctionDto dto = new FunctionDto();
            dto.setFunctionRoleId(ValueUtil.getIntegerByObject(obj[0]));
            dto.setFunctionId(ValueUtil.getIntegerByObject(obj[1]));
            dto.setScope(EScope.valueOf(obj[2].toString()));
            dto.setAction(EAction.valueOf(obj[3].toString()));
            dto.setName(ValueUtil.getStringByObject(obj[4]));
            functionDtoList.add(dto);
        }
        return functionDtoList;
    }

    @Override
    public List<Integer> findRoleIdsFromScopes(String... scope) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT fr.role_id "
                + "FROM function_role fr "
                + "INNER JOIN function f "
                + "ON f.function_id = fr.function_id "
                + "WHERE f.scope IN (:scopes) "
                + "GROUP BY fr.role_id ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("scopes", Arrays.asList(scope));

        return query.getResultList();
    }
}
