package com.gautami.authorization.service;

import com.gautami.authorization.Repository.RoleRepository;
import com.gautami.authorization.Repository.UserRepository;
import com.gautami.authorization.dto.AdminRequest;
import com.gautami.authorization.dto.UserDto;
import com.gautami.authorization.exception.AlreadyExists;
import com.gautami.authorization.exception.NotFoundException;
import com.gautami.authorization.model.Role;
import com.gautami.authorization.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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
            throw new AlreadyExists("A user with the given email already exists");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(userRequest.getEmail());
        Role role = roleRepository.findRoleByRoleName("ROLE_USER");
        if (role == null) {
            role = new Role();
            role.setRoleName("ROLE_USER");
            roleRepository.save(role);
        }

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        user.setRoles(roleSet);

        userRepository.save(user);
  }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        User user= userRepository.findById(id).get();
        if(user==null){
            throw  new NotFoundException("User with the given Id not found");
        }
        return user;
    }

    public void updateUser(Long id,UserDto userRequest) {
        User existingUser=userRepository.findByEmail(userRequest.getEmail());
        if(existingUser!=null&&existingUser.getId()!=id){
            throw new AlreadyExists("A user with the given email Id Already exists: "+userRequest.getEmail());
        }
        User user=userRepository.findById(id).get();
        BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());

        user.setUsername(userRequest.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(userRequest.getEmail());
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user= userRepository.findById(id).get();
        if(user==null){
            throw  new NotFoundException("User with the given Id not found");
        }
        userRepository.deleteById(id);
    }


    public void createAdminUser(AdminRequest request) {
        if (!userRepository.findByUsername("ADMIN").isPresent()) {
            User adminUser = new User();
            adminUser.setUsername(request.getUsername());
            BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();
            String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
            adminUser.setPassword(encodedPassword);
            adminUser.setEmail(request.getEmail());
            Role role=new Role();
            role.setRoleName("ROLE_ADMIN");
            Set<Role> roleSet=new HashSet<>();
            roleSet.add(role);
            adminUser.setRoles(roleSet);
            userRepository.save(adminUser);
        }
    }
}
