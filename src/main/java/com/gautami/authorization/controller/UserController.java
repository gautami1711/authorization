package com.gautami.authorization.controller;

import com.gautami.authorization.dto.AdminRequest;
import com.gautami.authorization.dto.UserDto;
import com.gautami.authorization.exception.InvalidRequest;
import com.gautami.authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

}
