package com.example.zscacm.controller;

import com.example.zscacm.entity.CfContests;
import com.example.zscacm.service.CfService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class CfContestsController {

    @Resource
    private CfService cfService;

    @GetMapping("/FutureContests")
    public List<CfContests> getFutureContests() {
        List<CfContests> list = cfService.getFutureContests();
        return list;
    }
}
