package com.example.zscacm.service;

import com.example.zscacm.entity.CfProblems;
import com.example.zscacm.mapper.CfProblemsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CfService {

    @Resource
    private CfProblemsMapper cfProblemsMapper;

    public CfProblems selectProblemByIds(int firstId, int secondId, int thirdId) {

        CfProblems cfProblems = cfProblemsMapper.selectByIds(firstId, secondId, thirdId);
        return cfProblems;
    }

    public int addProblem(CfProblems problem) {
        int id = cfProblemsMapper.insert(problem);
        return id;
    }
}
