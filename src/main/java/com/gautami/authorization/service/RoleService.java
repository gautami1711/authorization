package com.gautami.authorization.service;

import com.gautami.authorization.Repository.RoleRepository;
import com.gautami.authorization.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {


    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void createRoles(List<Role> role) {

        for(int i=0;i<role.size();i++){
            Optional<Role> ex= Optional.ofNullable(roleRepository.findRoleByRoleName(role.get(i).getRoleName()));
            if(!ex.isPresent()){
                roleRepository.save(role.get(i));
            }
        }
    }
}

