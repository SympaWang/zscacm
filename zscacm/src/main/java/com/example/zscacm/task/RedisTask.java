package com.example.zscacm.task;

import com.example.zscacm.service.UserService;
import com.example.zscacm.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisTask {

    @Resource
    private RedisCache redisCache;

    @Resource
    private UserService userService;

    @Scheduled(cron = "0/10 * * * * *")
    public void redisTimer() {
        redisCache.getCacheObject("heartbeat");
    }

    @Scheduled(cron = "0/10 * * * * *")
    public void sqlTimer() {
        userService.selectHeartbeat();
    }
}
