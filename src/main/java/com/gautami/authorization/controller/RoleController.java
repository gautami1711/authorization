package com.gautami.authorization.controller;

import com.gautami.authorization.model.Role;
import com.gautami.authorization.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PutMapping("/create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public  void createRoles(@RequestBody List<Role> role){
        roleService.createRoles(role);
    }
}
