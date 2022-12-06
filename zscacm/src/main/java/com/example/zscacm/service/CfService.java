package com.example.zscacm.service;

import com.example.zscacm.entity.CfProblems;
import com.example.zscacm.entity.CfProblemsType;
import com.example.zscacm.mapper.CfProblemsMapper;
import com.example.zscacm.mapper.CfProblemsTypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CfService {

    @Resource
    private CfProblemsMapper cfProblemsMapper;

    @Resource
    private CfProblemsTypeMapper cfProblemsTypeMapper;

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

}
