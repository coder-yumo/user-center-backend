package com.yupi.usercenterbackend.controller;

import com.yupi.usercenterbackend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("")
    public void login(){

    }

}
