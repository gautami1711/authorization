package com.gautami.authorization.controller;

import com.gautami.authorization.Repository.UserRepository;
import com.gautami.authorization.dto.AdminRequest;
import com.gautami.authorization.dto.UserDto;
import com.gautami.authorization.exception.Forbidden;
import com.gautami.authorization.exception.InvalidRequest;
import com.gautami.authorization.exception.NotFoundException;
import com.gautami.authorization.model.Role;
import com.gautami.authorization.model.User;
import com.gautami.authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${secretKey}")
    private String secretKey;

    private UserService userService;

    private UserRepository userRepository;


    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody UserDto userRequest) {
        userService.createUser(userRequest);
    }


    @PostMapping("/createadmin")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAdmin(@RequestBody AdminRequest request) {
        if (request.getKey().equals(secretKey)) {
            userService.createAdminUser(request);
            return;
        }
        //throw some error
        throw new InvalidRequest("The key given is not correct, please give correct key to proceed");

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }


    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public void updateUser(@PathVariable Long id, @RequestBody UserDto userRequest) {
        userService.updateUser(id, userRequest);
    }

    @DeleteMapping("/delete/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void deleteUser(@PathVariable Long id, @RequestParam(value = "userId") Long userId) {

        Optional<User> permitUser = userRepository.findById(userId);
        if (!permitUser.isPresent()) {
            throw new NotFoundException("The user try to delete the account is not found");
        }
        Set<Role> permittedRole = permitUser.get().getRoles();

        boolean flag = permittedRole.stream().anyMatch(role -> role.getRoleName().equalsIgnoreCase("ROLE_ADMIN"));


        if (flag || permitUser.get().getId() == id) {
            userService.deleteUser(id);
            return;
        }

        throw new Forbidden("You are not authorized to delete the user!!!!");
    }


}




