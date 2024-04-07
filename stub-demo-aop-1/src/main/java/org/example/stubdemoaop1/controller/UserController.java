package org.example.stubdemoaop1.controller;

import org.example.stubdemoaop1.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {
    @Resource
    private UserService userService;
    @GetMapping("/register")
    public String registe(String mobile){
        userService.registe(mobile);
        return "success";
    }
}