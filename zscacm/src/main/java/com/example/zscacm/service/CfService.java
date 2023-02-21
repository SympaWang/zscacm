package com.example.zscacm.service;

import com.example.zscacm.entity.*;
import com.example.zscacm.mapper.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CfService {

    @Resource
    private CfProblemsMapper cfProblemsMapper;

    @Resource
    private CfProblemsTypeMapper cfProblemsTypeMapper;

    @Resource
    private CfUserMapper cfUserMapper;

    @Resource
    private CfContestsMapper cfContestsMapper;

    @Resource
    private CfUserContestsMapper cfUserContestsMapper;

    public CfProblems selectProblemByIds(int firstId, int secondId, int thirdId) {

        CfProblems cfProblems = cfProblemsMapper.selectByIds(firstId, secondId, thirdId);
        return cfProblems;
    }

    public int addProblem(CfProblems problem) {
        return cfProblemsMapper.insert(problem);
    }

    public int addProblemType(int firstId, int secondId, int thirdId, String type) {
        CfProblemsType cfProblemsType = CfProblemsType.builder().firstId(firstId).secondId(secondId).thirdId(thirdId).type(type).build();
        return cfProblemsTypeMapper.insert(cfProblemsType);
    }

    public int selectUserId(String handle) {
        return cfUserMapper.selectUserIdByHandle(handle);
    }

    public CfUser selectUser(String handle) {
        return cfUserMapper.selectUserByHandle(handle);
    }

    public Integer updateUser(CfUser user) {
        return cfUserMapper.updateById(user);
    }

    public Integer addUser(CfUser user) {
        return cfUserMapper.insert(user);
    }


    public Integer addContest(List<CfContests> contests) {
        int addNum = 0;
        for(CfContests contest : contests) {
            CfContests cfContests = cfContestsMapper.selectById(contest.getId());
            if(cfContests == null) {
                cfContestsMapper.insert(contest);
                addNum += 1;
            } else {
                break;
            }
        }
        return addNum;
    }

    //获取未开始比赛列表
    public List<CfContests> getFutureContests() {
        List<CfContests> contests = cfContestsMapper.getFutureContests();
        return contests;
    }


    public Integer addUserContest(List<CfUserContest> userContests) {
        for(CfUserContest usercontest : userContests) {
            CfUserContest cfUserContest = cfUserContestsMapper.selectById(usercontest.getId());
            if(cfUserContest == null) {
                cfUserContestsMapper.insert(usercontest);
            } else {
                cfUserContestsMapper.updateById(usercontest);
            }
        }

        return userContests.size();
    }

    public List<CfProblems> selectCfProblems() {
        return cfProblemsMapper.selectList();
    }

    public CfProblemsType selectTypeByIdType(int firstId, int secondId, int thirdId, String type) {
        return cfProblemsTypeMapper.selectTypeByIdType(firstId, secondId, thirdId, type);
    }

    public List<String> selectTypeById(int firstId, int secondId, int thirdId) {
        return cfProblemsTypeMapper.selectTypeById(firstId, secondId, thirdId);
    }

    public List<CfProblemsType> selectTypeByType(String type) {
        return cfProblemsTypeMapper.selectTypeByType(type);
    }
}
