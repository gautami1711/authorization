package com.gautami.authorization.service;

import com.gautami.authorization.Repository.RoleRepository;
import com.gautami.authorization.Repository.UserRepository;
import com.gautami.authorization.dto.UserDto;
import com.gautami.authorization.model.Role;
import com.gautami.authorization.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public void createUser(UserDto userRequest) {

        User existingUser=userRepository.findByEmail(userRequest.getEmail());
        if(existingUser!=null){
            //throw some exception
            return;
        }

        User user = new User(userRequest.getPassword(),userRequest.getUsername(),userRequest.getEmail());
        Set<Role> roleSet = new HashSet<>();
        for(String r: userRequest.getRoleNames()){
            Role role=roleRepository.findRoleByRoleName(r.toUpperCase());
            if(role!=null){
                roleSet.add(role);
            }
        }
        if(roleSet.isEmpty()) {
            //throw some exception
            return;
        }
        user.setRoles(roleSet);
        userRepository.save(user);
    }
}
