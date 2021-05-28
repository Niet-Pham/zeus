package vn.tech.website.store.controller.admin.base;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ObjectUtils;
import vn.tech.website.store.controller.admin.BaseController;
import vn.tech.website.store.controller.admin.auth.AuthorizationController;
import vn.tech.website.store.dto.base.FunctionDto;
import vn.tech.website.store.dto.base.RoleSearchDto;
import vn.tech.website.store.dto.user.AccountDto;
import vn.tech.website.store.dto.user.AccountSearchDto;
import vn.tech.website.store.entity.EAction;
import vn.tech.website.store.entity.EScope;
import vn.tech.website.store.model.Account;
import vn.tech.website.store.model.Function;
import vn.tech.website.store.model.Role;
import vn.tech.website.store.repository.AccountRepository;
import vn.tech.website.store.repository.FunctionRepository;
import vn.tech.website.store.repository.FunctionRoleRepository;
import vn.tech.website.store.repository.RoleRepository;
import vn.tech.website.store.service.RoleService;
import vn.tech.website.store.util.DbConstant;
import vn.tech.website.store.util.FacesUtil;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@Named
@Scope(value = "session")
@Getter
@Setter
public class RoleController extends BaseController {
    @Inject
    private AuthorizationController authorizationController;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected RoleService roleService;
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private FunctionRoleRepository functionRoleRepository;
    @Autowired
    private AccountRepository accountRepository;

    private Role role;
    private LazyDataModel<Role> lazyDataModel;
    private RoleSearchDto roleSearchDto;
    private List<SelectItem> roleList;
    private Role roleCopy;
    private LazyDataModel<AccountDto> lazyDataModelAccount;
    private AccountSearchDto accountSearchDto;
    //
    private Map<EScope, List<EAction>> scopeActions;
    private Map<EScope, List<EAction>> scopeActionsAll;
    private Map<EScope, Boolean> stateScope;
    private List<Function> functionList;
    private boolean checkboxSelectAll;
    private List<FunctionDto> functionDtoList;
    private Long roleId;

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            init();
            resetAll();
        }
    }

    public void resetAll() {
        roleList = new ArrayList<>();
        role = new Role();
        roleCopy = new Role();
        roleSearchDto = new RoleSearchDto();
        scopeActions = new HashMap<>();
        scopeActionsAll = new HashMap<>();
        stateScope = new HashMap<>();
        checkboxSelectAll = false;
        //build function list
        buildScopes();
        //reset table
        onSearch();
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
    }

    public void onSaveData(Long roleId ) {
        if (role.getRoleId() != null && checkEditButton(roleId)) {
            if (role.getStatus() == DbConstant.ROLE_STATUS_INACTIVE) {
                FacesUtil.addErrorMessage("Không được ngừng hoạt động vì nhóm quyền " + role.getName() + " đang được gán cho người dùng");
                FacesUtil.updateView("growl");
                return;
            }
        }
        roleRepository.save(role);
        //save role's functions
        functionDtoList = new ArrayList<>();
        for (Map.Entry<EScope, List<EAction>> e : scopeActions.entrySet()) {
            if (ObjectUtils.isEmpty(e.getValue())) {
                continue;
            }
            buildFunctionRole(e);
        }

        roleService.save(role, functionDtoList);

        //notify
        setSuccessForm("Lưu thành công");
        FacesUtil.closeDialog("dialogInsertUpdate");
        resetAll();
        FacesUtil.updateView("growl");
        FacesUtil.updateView("searchForm");

    }

    public void resetDialog(){
        roleList = new ArrayList<>();
        role = new Role();
        roleCopy = new Role();
        roleSearchDto = new RoleSearchDto();
        scopeActions = new HashMap<>();
        scopeActionsAll = new HashMap<>();
        stateScope = new HashMap<>();
        checkboxSelectAll = false;
        //build function list
        buildScopes();
    }

    public void showUpdatePopup(Role obj) {
        if (obj.getRoleId() != null) {
            buildScopes();
            loadScopeAction(obj.getRoleId());
        }
        roleCopy = new Role();
        BeanUtils.copyProperties(obj, role);
        BeanUtils.copyProperties(obj, roleCopy);
    }

    public void onDelete(Role obj) {
        try {
            if (!obj.getCanModify()) {
                setErrorForm("Không thể xóa nhóm quyền mặc định");
                return;
            }
            List<Account> accountExists = accountRepository.findAccountByRoleId(obj.getRoleId());
            if (!accountExists.isEmpty()) {
                setErrorForm("Không được xóa vì nhóm quyền đang được gán cho người dùng");
                return;
            }
            roleService.delete(obj.getRoleId());
            onSearch();
            FacesUtil.addSuccessMessage("Xóa nhóm quyền thành công");
        } catch (Exception e) {
            FacesUtil.addErrorMessage("Xóa nhóm quyền thất bại");
        }
    }

    public void onSearch() {
        FacesUtil.resetDataTable("searchForm", "tblSearchResult");
        lazyDataModel = new LazyDataModel<Role>() {
            @Override
            public List<Role> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                roleSearchDto.setPageIndex(first);
                roleSearchDto.setPageSize(pageSize);
                roleSearchDto.setSortField(sortField);
                String sort = "";
                if (sortOrder.equals(SortOrder.ASCENDING)) {
                    sort = "ASC";
                } else {
                    sort = "DESC";
                }
                roleSearchDto.setSortOrder(sort);
                return roleRepository.search(roleSearchDto);
            }

            @Override
            public Role getRowData(String rowKey) {
                List<Role> roleList = getWrappedData();
                String value = String.valueOf(rowKey);
//                for (Role obj : roleList) {
//                    if (obj.getName().equals(value) || obj.getName().equals(value)) {
//
//                    }
//
//                }
                return null;
            }
        };
        int count = roleRepository.countSearch(roleSearchDto).intValue();
        lazyDataModel.setRowCount(count);
    }

    public void loadAccountList(Integer roleId) {
        accountSearchDto = new AccountSearchDto();
        accountSearchDto.setRoleId(roleId);

        lazyDataModelAccount = new LazyDataModel<AccountDto>() {
            @Override
            public List<AccountDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                accountSearchDto.setPageIndex(first);
                accountSearchDto.setPageSize(pageSize);
                accountSearchDto.setSortField(sortField);
                String sort = "";
                if (sortOrder.equals(SortOrder.ASCENDING)) {
                    sort = "ASC";
                } else {
                    sort = "DESC";
                }
                accountSearchDto.setSortOrder(sort);
                return accountRepository.search(accountSearchDto);
            }
        };
        int count = accountRepository.countSearch(accountSearchDto).intValue();
        lazyDataModelAccount.setRowCount(count);

        FacesUtil.resetDataTable("dlForm1", "tblSearchResult1");
    }

    public void selectAllByScope(EScope scope) {
        scopeActions.get(scope).clear();
        if (stateScope.get(scope)) {
            scopeActions.get(scope).addAll(functionList
                    .stream()
                    .filter(e -> e.getScope().equals(scope))
                    .map(Function::getAction)
                    .collect(Collectors.toList()));
        }
    }

    public void selectAll() {
        List<String> temp = new ArrayList<>();
        functionList.forEach(e -> {
            if (temp.contains(e.getScope().getScope())) {
                return;
            }
            temp.add(e.getScope().getScope());
            stateScope.replace(e.getScope(), checkboxSelectAll);
            selectAllByScope(e.getScope());
        });
    }

    private void loadScopeAction(Integer roleId) {
        //reset scope
        buildScopes();
        //load scope, action from db
//        for (Function function : authorizationController.getAuthFunction().getFunctions(roleId).getFunctionList()) {
//            EScope scope = function.getScope();
//            EAction action = function.getAction();
//            scopeActions.get(scope).add(action);
//        }
        //set select all if selected all
        int temp = 0;
        for (Map.Entry<EScope, List<EAction>> e : scopeActions.entrySet()) {
            if (e.getValue().size() == scopeActionsAll.get(e.getKey()).size()) {
                stateScope.replace(e.getKey(), true);
                temp++;
            }
        }
        checkboxSelectAll = stateScope.size() == temp;
    }

    private void buildFunctionRole(Map.Entry<EScope, List<EAction>> e) {
        if (e.getValue() instanceof ArrayList) {
            for (Function function : functionList) {
                for (Object o : e.getValue()) {
                    if (function.getScope().equals(e.getKey()) && o.equals(function.getAction())) {
                        FunctionDto functionDto = new FunctionDto();
                        BeanUtils.copyProperties(function, functionDto);
                        functionDtoList.add(functionDto);
                    }
                }
            }
        } else {
            Object[] array = (Object[]) Collections.singletonList(e.getValue()).toArray()[0];
            for (Function function : functionList) {
                for (Object o : array) {
                    if (function.getScope().equals(e.getKey()) && o.equals(function.getAction())) {
                        FunctionDto functionDto = new FunctionDto();
                        BeanUtils.copyProperties(function, functionDto);
                        functionDtoList.add(functionDto);
                    }
                }
            }
        }
    }

    private void buildScopes() {
        functionList = Lists.newArrayList(functionRepository.findAll());
        scopeActionsAll.clear();
        checkboxSelectAll = false;
        //init function select cache
        functionList.forEach(e -> {
            stateScope.put(e.getScope(), false);
            scopeActions.put(e.getScope(), new ArrayList<>());
            scopeActionsAll.putIfAbsent(e.getScope(), new ArrayList<>());
            scopeActionsAll.get(e.getScope()).add(e.getAction());
        });
    }
    public boolean checkEditButton(Long roleId) {
        List<Long> listroleId = accountRepository.findRoleId();
        if (listroleId.contains(roleId)) {
            return true;
        }
        return false;
    }

    @Override
    protected EScope getMenuId() {
        return EScope.ROLE;
    }

}


