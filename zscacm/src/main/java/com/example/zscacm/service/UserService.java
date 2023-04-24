package com.example.zscacm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.zscacm.entity.SysUser;
import com.example.zscacm.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public int selectHeartbeat() {

        return sysUserMapper.selectHeartbeat();
    }

    public int updateUser(SysUser user) {

        return sysUserMapper.updateById(user);
    }

    public int addUserList(List<SysUser> userList) {
        int ans = 0;
        for(SysUser user : userList) {
            BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            user.setPassword(bc.encode("123456"));
            user.setCreateTime(new Date());
            user.setUserType(2);

            Integer uid = sysUserMapper.selectIdByName(user.getUsername());

            if(uid == null && sysUserMapper.insert(user) != 0) {
                ans++;
            }
        }
        return ans;
    }

    public boolean matchPassword(int uid, String password) {
        SysUser user = sysUserMapper.selectById(uid);
        if(user == null) return false;

        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();

        return bc.matches(password, user.getPassword());
    }

}
