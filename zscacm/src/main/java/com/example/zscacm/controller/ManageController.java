package com.example.zscacm.controller;

import com.example.zscacm.entity.SysUser;
import com.example.zscacm.service.UserService;
import com.example.zscacm.utils.ResponseResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @GetMapping("getUserDetail")
    public ResponseResult getUserDetail(@RequestParam(value = "uid", required = false) Integer uid) {

        SysUser user = userService.selectUserById(uid);

        return new ResponseResult(200, "查询成功", user);
    }

    @PostMapping("updateUserDetail")
    public ResponseResult updateUserDetail(@RequestBody SysUser sysUser) {

        if(sysUser == null) {
            return new ResponseResult(400, "用户不存在", null);
        }

        SysUser user = userService.selectUserById(sysUser.getId());

        if(user != null) {
            userService.updateUser(sysUser);
            return new ResponseResult(200, "修改成功", null);
        } else {
            return new ResponseResult(400, "用户不存在", null);
        }
    }

    @PostMapping("addUser")
    public ResponseResult addUser(@RequestBody List<SysUser> userList) {

        if(userList == null) {
            return new ResponseResult(400, "用户列表为空！", null);
        }

        int result = userService.addUserList(userList);

        return new ResponseResult(200, "增加用户成功", result);
    }

    @PostMapping("changePassword")
    public ResponseResult changePassword(@RequestBody Map<String,Object> map) {
        int uid = (int) map.get("uid");
        String oldPassword = (String) map.get("oldPassword");
        String newPassword = (String) map.get("newPassword");

        SysUser user = userService.selectUserById(uid);

        if(user == null) {
            return new ResponseResult(400, "用户不存在", null);
        }

        boolean matches = userService.matchPassword(uid, oldPassword);

        if(!matches) {
            return new ResponseResult(400, "原密码错误", null);
        } else {
            BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            user.setPassword(bc.encode(newPassword));
            userService.updateUser(user);
            return new ResponseResult(200, "密码修改成功", null);
        }
    }
}
