package com.gautami.authorization.service;

import com.gautami.authorization.Repository.RoleRepository;
import com.gautami.authorization.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;


    public void createRoles(List<Role> role) {
        for(Role r:role){
            if(roleRepository.findRoleByRoleName(r.getRoleName())==null) {
                roleRepository.save(r);
            }
        }
    }
}
