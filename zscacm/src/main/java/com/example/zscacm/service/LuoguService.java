package com.example.zscacm.service;

import com.example.zscacm.entity.LuoguUser;
import com.example.zscacm.entity.LuoguUserProblem;
import com.example.zscacm.mapper.LuoguUserMapper;
import com.example.zscacm.mapper.LuoguUserProblemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LuoguService {

    @Autowired
    LuoguUserMapper luoguUserMapper;

    @Autowired
    LuoguUserProblemMapper luoguUserProblemMapper;


    public LuoguUser selectUserByLgid(int lgid) {
        LuoguUser user = luoguUserMapper.selectByLgid(lgid);
        return user;
    }

    public Integer selectTotalProblemByLgid(int lgid) {
        return luoguUserMapper.selectTotalProblemByLgid(lgid);
    }

    public int addUser(int lgid) {
        LuoguUser user = LuoguUser.builder().lgid(lgid).addTime(new Date()).build();
        luoguUserMapper.insert(user);
        for(int i = 1; i <= 8; i++) {
            LuoguUserProblem data = LuoguUserProblem.builder().lgid(lgid).problemType(i).addTime(new Date()).build();
            luoguUserProblemMapper.insert(data);
        }
        return user.getUid();
    }

    public void updateSolved(int lgid, List<Integer> num, Integer totalProblems) {
        LuoguUser user = luoguUserMapper.selectByLgid(lgid);
        user.setTotalProblems(totalProblems);
        luoguUserMapper.updateById(user);
        for(int i = 1; i <= 8; i++) {
            int problemNum = num.get(i - 1);
            LuoguUserProblem userProblem = luoguUserProblemMapper.selectProblemByType(lgid, i);
            userProblem.setProblemNum(problemNum);
            luoguUserProblemMapper.updateById(userProblem);
        }
    }

    public List<LuoguUserProblem> selectSolvedByLgid(int lgid) {
        return luoguUserProblemMapper.selectProblemByLgid(lgid);
    }

}
