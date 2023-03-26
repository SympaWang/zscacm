package com.example.zscacm.controller;

import com.example.zscacm.entity.SysUser;
import com.example.zscacm.service.UserService;
import com.example.zscacm.utils.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ManageController {

    @Resource
    private UserService userService;

    @GetMapping("getUserDetailList")
    public ResponseResult getUserDetailList(@RequestParam(value = "grade", required = false) Integer grade) {

        List<SysUser> userList;
        if(grade == null) {
            userList = userService.selectUser();
        } else {
            userList = userService.selectUserByGrade(grade);
        }

        return new ResponseResult(200, "查询成功", userList);
    }
}
