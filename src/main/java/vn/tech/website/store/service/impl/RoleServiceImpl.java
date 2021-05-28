package vn.tech.website.store.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.tech.website.store.dto.base.FunctionDto;
import vn.tech.website.store.model.FunctionRole;
import vn.tech.website.store.model.Role;
import vn.tech.website.store.repository.FunctionRepository;
import vn.tech.website.store.repository.FunctionRoleRepository;
import vn.tech.website.store.repository.RoleRepository;
import vn.tech.website.store.service.RoleService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private FunctionRoleRepository functionRoleRepository;
    @Autowired
    private FunctionRepository functionRepository;

    @Override
    @Transactional
    public void save(Role role, List<FunctionDto> saveFunctionRoleList) {
        Integer roleId = role.getRoleId();
        List<FunctionDto> oldFunctionList = functionRepository.findFunctionsByRoleId(roleId);
        List<FunctionRole> deleteFunctionRoleList = new ArrayList<>();
        List<FunctionRole> createFunctionRoleList = new ArrayList<>();
        //build old + delete
        for (FunctionDto oldFunction : oldFunctionList) {
            FunctionRole functionRole = new FunctionRole(oldFunction.getFunctionRoleId(), roleId, oldFunction.getFunctionId());
            if (saveFunctionRoleList.stream().anyMatch(e -> oldFunction.getScope().equals(e.getScope()) && oldFunction.getAction().equals(e.getAction()))) {
                createFunctionRoleList.add(functionRole);
            } else {
                deleteFunctionRoleList.add(functionRole);
            }
        }
        //build new role's functions
        for (FunctionDto newFunctionDto : saveFunctionRoleList) {
            if (oldFunctionList.stream().noneMatch(e -> newFunctionDto.getScope().equals(e.getScope()) && newFunctionDto.getAction().equals(e.getAction()))) {
                createFunctionRoleList.add(new FunctionRole(newFunctionDto.getFunctionRoleId(), roleId, newFunctionDto.getFunctionId()));
            }
        }
        //save
        functionRoleRepository.deleteAll(deleteFunctionRoleList);
        functionRoleRepository.saveAll(createFunctionRoleList);
    }

    @Override
    @Transactional
    public void delete(Integer roleId) {
        //remove from role table
        roleRepository.deleteById(roleId);
        //remove from function_role table
        functionRoleRepository.deleteByRoleId(roleId);
    }
}
