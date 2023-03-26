package com.example.zscacm.task;

import com.example.zscacm.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisTask {

    @Resource
    private RedisCache redisCache;

    @Scheduled(cron = "0/10 * * * * *")
    public void timer() {
        redisCache.getCacheObject("heartbeat");
    }
}
