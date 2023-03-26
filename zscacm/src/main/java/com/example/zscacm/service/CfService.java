package com.example.zscacm.service;

import com.example.zscacm.entity.*;
import com.example.zscacm.mapper.*;
import com.example.zscacm.utils.RedisCache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Resource
    private CfUserSubmitsMapper cfUserSubmitsMapper;

    @Resource
    private RedisCache redisCache;

    public CfProblems selectProblemByIds(int firstId, int secondId, int thirdId) {

        CfProblems cfProblems = redisCache.getCacheObject("cfProblem:" + firstId + ":" + secondId + ":" + thirdId);
        if(cfProblems == null) {
            cfProblems = cfProblemsMapper.selectByIds(firstId, secondId, thirdId);
            redisCache.setCacheObject("cfProblem:" + firstId + ":" + secondId + ":" + thirdId, cfProblems);
            redisCache.expire("cfProblem:" + firstId + ":" + secondId + ":" + thirdId, 60 * 60 * 12);
        }

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
                cfContestsMapper.updateTimeById(contest.getRelativeTime(), contest.getId());
            }
        }
        return addNum;
    }

    //获取未开始比赛列表
    public List<CfContests> getFutureContests() {

        List<CfContests> contests = redisCache.getCacheList("futureContests");
        if(contests == null || contests.size() == 0) {

            contests = cfContestsMapper.getFutureContests(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

            for(CfContests contest : contests) {
                contest.setRelativeTime((int) ((new Date().getTime() - contest.getBeginTime().getTime()) / 1000));
            }

            redisCache.setCacheList("futureContests", contests);
            redisCache.expire("futureContests", 60 * 60);
        }

        return contests;
    }


    public Integer addUserContest(List<CfUserContest> userContests) {
        for(CfUserContest usercontest : userContests) {
            CfUserContest cfUserContest = cfUserContestsMapper.selectByUser(usercontest.getContestId(), usercontest.getHandle());
            if(cfUserContest == null) {
                cfUserContestsMapper.insert(usercontest);
            } else {
                usercontest.setId(cfUserContest.getId());
                cfUserContestsMapper.updateById(usercontest);
            }
        }

        return userContests.size();
    }

    public List<CfProblems> selectCfProblems() {

        List<CfProblems> cfProblems = redisCache.getCacheList("cfProblemList");
        if(cfProblems == null || cfProblems.size() == 0) {
            cfProblems = cfProblemsMapper.selectList();
            redisCache.setCacheList("cfProblemList", cfProblems);
            redisCache.expire("cfProblemList", 60 * 60 * 12);
        }

        return cfProblems;
    }

    public CfProblemsType selectTypeByIdType(int firstId, int secondId, int thirdId, String type) {
        return cfProblemsTypeMapper.selectTypeByIdType(firstId, secondId, thirdId, type);
    }

    public List<String> selectTypeById(int firstId, int secondId, int thirdId) {

        List<String> cfProblemType = redisCache.getCacheList("cfProblemType:" + firstId + ":" + secondId + ":" + thirdId);
        if(cfProblemType == null || cfProblemType.size() == 0) {
            cfProblemType = cfProblemsTypeMapper.selectTypeById(firstId, secondId, thirdId);
            redisCache.setCacheList("cfProblemType:" + firstId + ":" + secondId + ":" + thirdId, cfProblemType);
            redisCache.expire("cfProblemType:" + firstId + ":" + secondId + ":" + thirdId, 60 * 60 * 12);
        }

        return cfProblemsTypeMapper.selectTypeById(firstId, secondId, thirdId);
    }

    public List<CfProblemsType> selectTypeByType(String type) {
        return cfProblemsTypeMapper.selectTypeByType(type);
    }

    public List<CfUserContest> selectUserContestsByHandle(String handle) {
        return cfUserContestsMapper.selectUserContestsByHandle(handle);
    }

    public String selectContestNameById(Integer id) {
        return cfContestsMapper.selectContestNameById(id);
    }

    public int selectProblemCount() {
        return cfProblemsMapper.selectCount();
    }

    public List<CfUserContest> selectContestUsersById(Integer contestId) {
        return cfUserContestsMapper.selectContestUsersById(contestId);
    }

    public List<CfContests> selectEndContests() {
        List<CfContests> contests = redisCache.getCacheList("endContests");
        if(contests == null || contests.size() == 0) {
            contests = cfContestsMapper.selectEndContestList();
            redisCache.setCacheList("endContests", contests);
            redisCache.expire("endContests", 60 * 60);
        }

        return contests;
    }

    public Integer updateDifById(Integer dif, Integer firstId, Integer secondId, Integer thirdId) {
        return cfProblemsMapper.updateDifById(dif, firstId, secondId, thirdId);
    }

    public CfUserSubmits selectSubmitById(Integer cfId, Integer firstId, Integer secondId, Integer thirdId) {

        CfUserSubmits submit = redisCache.getCacheObject("cfSubmit:" + cfId + ":" + firstId + ":" + secondId + ":" + thirdId);
        if(submit == null) {
            submit = cfUserSubmitsMapper.selectSubmitById(cfId, firstId, secondId, thirdId);
            redisCache.setCacheObject("cfSubmit:" + cfId + ":" + firstId + ":" + secondId + ":" + thirdId, submit);
            redisCache.expire("cfSubmit:" + cfId + ":" + firstId + ":" + secondId + ":" + thirdId, 60 * 60 * 12);
        }

        return cfUserSubmitsMapper.selectSubmitById(cfId, firstId, secondId, thirdId);
    }

    public Integer insertUserSubmit(CfUserSubmits cfUserSubmits) {
        return cfUserSubmitsMapper.insert(cfUserSubmits);
    }

    public Integer updateSubmitById(Integer cfId, Integer firstId, Integer secondId, Integer thirdId, String status, Integer creationTime){
        return cfUserSubmitsMapper.updateSubmitById(cfId, firstId, secondId, thirdId, status, creationTime);
    }

    public List<CfUserSubmits> selectUserSubmit(Integer cfId) {

        List<CfUserSubmits> userSubmits = redisCache.getCacheList("userSubmits");

        if(userSubmits == null || userSubmits.size() == 0) {

            userSubmits = cfUserSubmitsMapper.selectUserSubmit(cfId);
            redisCache.setCacheList("userSubmits", userSubmits);
            redisCache.expire("userSubmits", 60 * 30);
        }

        return userSubmits;
    }

    public Integer selectUserContestNum(String handle) {

        return cfUserContestsMapper.selectUserContestNum(handle);
    }

    public List<CfProblemIds> selectUserSubmitIds(Integer id) {

        return cfUserSubmitsMapper.selectUserSubmitIds(id);
    }
}
