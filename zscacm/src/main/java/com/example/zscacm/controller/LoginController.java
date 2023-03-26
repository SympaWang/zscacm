package com.example.zscacm.controller;

import com.example.zscacm.entity.SysUser;
import com.example.zscacm.service.LoginService;
import com.example.zscacm.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginService loginServcie;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody SysUser user){
        return loginServcie.login(user);
    }

    @GetMapping("/logout")
    public ResponseResult logout(){
        return loginServcie.logout();
    }

}
