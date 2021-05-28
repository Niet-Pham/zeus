package vn.tech.website.store.entity;

import lombok.Getter;
import lombok.Setter;
import vn.tech.website.store.dto.base.FunctionDto;

import java.util.List;

@Getter
@Setter
public class EFunction {
    private List<FunctionDto> functionList;
    private String scope;

    public EFunction(List<FunctionDto> functionList, String scope) {
        this.functionList = functionList;
        this.scope = scope;
    }

    public boolean canCreate() {
        scopeNotDefined();
        return hasAction(EAction.CREATE);
    }

    public boolean canUpdate() {
        scopeNotDefined();
        return hasAction(EAction.UPDATE);
    }

    public boolean canDelete() {
        scopeNotDefined();
        return hasAction(EAction.DELETE);
    }

    public boolean canSearch() {
        scopeNotDefined();
        return hasAction(EAction.SEARCH);
    }

    public boolean canDetail() {
        scopeNotDefined();
        return hasAction(EAction.DETAIL);
    }

    public boolean canSync() {
        scopeNotDefined();
        return hasAction(EAction.SYNC);
    }

    public boolean hasAction(EAction... actions) {
        //only for testing
//        return true;
        //TO-DO: uncomment in production mode
        scopeNotDefined();
        for (EAction action : actions) {
            if (functionList.stream()
                    .anyMatch(e -> e.getScope().toString().equals(this.scope)
                            && e.getAction().equals(action))) {
                return true;
            }
        }
        return false;
    }

    private void scopeNotDefined() {
        if (this.scope == null) {
            throw new UnsupportedOperationException("Scope is not defined.");
        }
    }
}
