package com.example.zscacm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.zscacm.entity.SysUser;
import com.example.zscacm.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    public List<SysUser> selectUserByGrade(int grade) {
        return sysUserMapper.selectUserByGrade(grade);
    }

    public List<SysUser> selectUser() {
        return sysUserMapper.selectUser();
    }

    public String selectHandleByName(String name) {
        return sysUserMapper.selectHandleByName(name);
    }

    public String selectNameByHandle(String handle) {
        return sysUserMapper.selectNameByHandle(handle);
    }

    public Integer selectIdByName(String name) {

        return sysUserMapper.selectIdByName(name);
    }

    public SysUser selectUserById(int id) {

        return sysUserMapper.selectById(id);
    }

    public String selectNameById(int id) {

        return sysUserMapper.selectNameById(id);
    }
}
