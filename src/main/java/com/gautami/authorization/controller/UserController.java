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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public void registerUser(@Valid @RequestBody UserDto userRequest, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors("username")) {
            throw new InvalidRequest("Username should not be empty and should have a minimum length of 3 characters.");
        }

        if (bindingResult.hasFieldErrors("password")) {
            throw new InvalidRequest("Password should not be empty and should have a minimum length of 8 characters.");
        }

        if (bindingResult.hasFieldErrors("email")) {
            throw new InvalidRequest("Please enter a valid value for email address.");
        }

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
    public void updateUser(@Valid @RequestBody UserDto userRequest,BindingResult bindingResult,@PathVariable Long id,
                           @RequestParam(value = "userId") Long userId) {

        //validating for the data sent for username, password, email follows is standard
        if (bindingResult.hasFieldErrors("username")) {
            throw new InvalidRequest("Username should not be empty and should have a minimum length of 3 characters.");
        }

        if (bindingResult.hasFieldErrors("password")) {
            throw new InvalidRequest("Password should not be empty and should have a minimum length of 8 characters.");
        }

        if (bindingResult.hasFieldErrors("email")) {
            throw new InvalidRequest("Please enter a valid value for email address.");
        }

        //checking if the user trying to  update the user with id coming as path variable has access to do this or not
        Optional<User> permitUser = userRepository.findById(userId);
        if (!permitUser.isPresent()) {
            throw new NotFoundException("The user trying  to update the account is not found. So no access to update.");
        }
        Set<Role> permittedRole = permitUser.get().getRoles();

        boolean flag = permittedRole.stream().anyMatch(role -> role.getRoleName().equals(Role.RoleName.ROLE_ADMIN));


        if (flag || permitUser.get().getId() == id) {
            userService.updateUser(id,userRequest);
            return;
        }

        throw new Forbidden("You are not authorized to delete the user!!!!");
    }

    @DeleteMapping("/delete/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void deleteUser(@PathVariable Long id, @RequestParam(value = "userId") Long userId) {

        Optional<User> permitUser = userRepository.findById(userId);
        if (!permitUser.isPresent()) {
            throw new NotFoundException("The user trying  to delete the account is not found. So no access to delete.");
        }
        Set<Role> permittedRole = permitUser.get().getRoles();

        boolean flag = permittedRole.stream().anyMatch(role -> role.getRoleName().equals(Role.RoleName.ROLE_ADMIN));


        if (flag || permitUser.get().getId() == id) {
            userService.deleteUser(id);
            return;
        }

        throw new Forbidden("You are not authorized to delete the user!!!!");
    }


}




