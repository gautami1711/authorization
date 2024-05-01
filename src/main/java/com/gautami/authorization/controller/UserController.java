package com.gautami.authorization.controller;

import com.gautami.authorization.dto.AdminRequest;
import com.gautami.authorization.dto.UserDto;
import com.gautami.authorization.exception.InvalidRequest;
import com.gautami.authorization.model.User;
import com.gautami.authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static  final String keyForAdmin="fajbfuqwbfuwbeuicbwiuebciuwbefuibwuifbwbfiwb3fiubwiqubfiu3qwbfuiwbfjeafjkbwibfhiawf";

    @Autowired
    UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody UserDto userRequest) {
        userService.createUser(userRequest);
    }


    @PostMapping("/createadmin")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAdmin(@RequestBody AdminRequest request){
        if(request.getKey().equals(keyForAdmin)) {
            userService.createAdminUser(request);
        }else{
            //throw some error
            throw new InvalidRequest("The key given is not correct, please give correct key to proceed");
        }
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
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }


    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public void updateUser(@PathVariable Long id,@RequestBody UserDto userRequest){
        userService.updateUser(id,userRequest);
    }

    @DeleteMapping("/delete/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }



}
