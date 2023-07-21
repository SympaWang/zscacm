package com.example.zscacm.service;

import com.example.zscacm.entity.DailySolve;
import com.example.zscacm.mapper.DailySolveMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DailySolveService {
    @Resource
    private DailySolveMapper dailySolveMapper;

    public int solveRecord(DailySolve dailySolve) {
        return dailySolveMapper.insert(dailySolve);
    }
}
