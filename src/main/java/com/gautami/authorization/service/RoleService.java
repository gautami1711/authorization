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
        for(int i=0;i<role.size();i++){
            String roleName=role.get(i).getRoleName().toUpperCase();
            if(!roleName.equalsIgnoreCase("ADMIN")){
                roleName="ROLE_"+roleName;
                role.get(i).setRoleName(roleName);
                roleRepository.save(role.get(i));
            }
        }
    }
}

