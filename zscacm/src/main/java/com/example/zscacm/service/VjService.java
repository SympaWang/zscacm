package com.example.zscacm.service;

import com.example.zscacm.entity.VjUser;
import com.example.zscacm.mapper.SysUserMapper;
import com.example.zscacm.mapper.VjUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VjService {

    @Autowired
    private VjUserMapper vjUserMapper;


    public VjUser selectUserByName(String vjName) {
        return vjUserMapper.selectVjUserByName(vjName);
    }

    public Integer selectTotalProblemByName(String vjName) {
        return vjUserMapper.selectTotalProblemByName(vjName);
    }
}
