package com.example.zscacm.controller;

import com.example.zscacm.entity.CfUser;
import com.example.zscacm.entity.LuoguUser;
import com.example.zscacm.entity.SysUser;
import com.example.zscacm.entity.VjUser;
import com.example.zscacm.service.CfService;
import com.example.zscacm.service.LuoguService;
import com.example.zscacm.service.UserService;
import com.example.zscacm.service.VjService;
import com.example.zscacm.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AcNumController {

    @Autowired
    private UserService userService;

    @Autowired
    private CfService cfService;

    @Autowired
    private LuoguService luoguService;

    @Autowired
    private VjService vjService;

    @GetMapping("/ac/cf")
    public ResponseResult getCfAccepts(@RequestParam(value = "grade", required = false) Integer grade) {
        List<SysUser> cfUserList;

        if(grade != null) {
            cfUserList = userService.selectUserByGrade(grade);
        } else {
            cfUserList = userService.selectUser();
        }

        List<Map<String, Object>> user = new ArrayList<>();

        for(SysUser sysUser : cfUserList) {
            Map<String, Object> userMap = new HashMap<>();
            if(sysUser.getHandle() == null) continue;

            CfUser cfUser = cfService.selectUser(sysUser.getHandle());
            if(cfUser != null) {
                userMap.put("name", sysUser.getUsername());
                userMap.put("handle", sysUser.getHandle());
                userMap.put("rating", cfUser.getRating());
                userMap.put("acNum", cfUser.getSolvedNum());
                user.add(userMap);
            }
        }

        return new ResponseResult(200, "查询成功", user);
    }

    @GetMapping("/ac/luogu")
    public ResponseResult getLuoguAccepts(@RequestParam(value = "grade", required = false) Integer grade) {
        List<SysUser> luoguUserList;

        if(grade != null) {
            luoguUserList = userService.selectUserByGrade(grade);
        } else {
            luoguUserList = userService.selectUser();
        }

        List<Map<String, Object>> user = new ArrayList<>();

        for(SysUser sysUser : luoguUserList) {
            Map<String, Object> userMap = new HashMap<>();
            if(sysUser.getLgid() == null) continue;

            LuoguUser luoguUser = luoguService.selectUserByLgid(sysUser.getLgid());
            if(luoguUser != null) {
                userMap.put("name", sysUser.getUsername());
                userMap.put("lgid", sysUser.getLgid());
                userMap.put("acNum", luoguUser.getTotalProblems());
                userMap.put("solves", luoguService.selectSolvedByLgid(sysUser.getLgid()));
                user.add(userMap);
            }
        }

        return new ResponseResult(200, "查询成功", user);
    }

    @GetMapping("/ac/vj")
    public ResponseResult getVjAccepts(@RequestParam(value = "grade", required = false) Integer grade) {
        List<SysUser> VjUserList;

        if(grade != null) {
            VjUserList = userService.selectUserByGrade(grade);
        } else {
            VjUserList = userService.selectUser();
        }

        List<Map<String, Object>> user = new ArrayList<>();

        for(SysUser sysUser : VjUserList) {
            Map<String, Object> userMap = new HashMap<>();
            if(sysUser.getVjName() == null) continue;

            VjUser vjUser = vjService.selectUserByName(sysUser.getVjName());
            if(vjUser != null) {
                userMap.put("name", sysUser.getUsername());
                userMap.put("vjName", sysUser.getVjName());
                userMap.put("weekProblem", vjUser.getWeekProblem());
                userMap.put("monthProblem", vjUser.getMonthProblem());
                userMap.put("totalProblem", vjUser.getTotalProblem());
                user.add(userMap);
            }
        }

        return new ResponseResult(200, "查询成功", user);
    }

}
