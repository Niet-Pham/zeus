package vn.tech.website.store.controller.admin.auth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import vn.tech.website.store.dto.base.FunctionDto;
import vn.tech.website.store.entity.EAction;
import vn.tech.website.store.entity.EFunction;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.Function;
import vn.tech.website.store.repository.FunctionRepository;
import vn.tech.website.store.util.DbConstant;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Scope(value = "session")
public class AuthFunctionController implements Serializable {
    @Autowired
    private FunctionRepository functionRepository;

    @Getter
    private EFunction eFunction;
    private Integer roleId;

    public void setRole(Integer roleId) {
        this.roleId = roleId;
        eFunction = getFunctions(roleId);
    }

    public EFunction scope(EScope scope) {
        eFunction.setScope(scope.getScope());
        return eFunction;
    }

    public boolean hasScope(EScope... scopes) {
        //only for testing
//        return true;
        //TO-DO: uncomment in production mode
        if (roleId == null || roleId == DbConstant.ROLE_ID_NOT_LOGIN) {
            return false;
        }
        //scope public
        if (EScope.PUBLIC.equals(scopes[0])) {
            return true;
        }
        if (eFunction == null) {
            return false;
        }
        for (EScope scope : scopes) {
            if (eFunction.getFunctionList()
                    .stream()
                    .anyMatch(e -> e.getScope().equals(scope))) {
                return true;
            }
        }
        return false;
    }

    public EFunction getFunctions(Integer roleId) {
        List<FunctionDto> function = functionRepository.findFunctionsByRoleId(roleId);
        return new EFunction(function, null);
    }

    public List<EAction> getActions(EScope scope) {
        return getActions(eFunction, scope);
    }

    private List<EAction> getActions(EFunction eFunction, EScope scope) {
        List<Function> fnc = eFunction.getFunctionList()
                .stream()
                .filter(e -> e.getScope().equals(scope))
                .collect(Collectors.toList());
        List<EAction> action = new ArrayList<>();
        for (Function fTemp : fnc) {
            action.add(fTemp.getAction());
        }
        return action;
    }
}
