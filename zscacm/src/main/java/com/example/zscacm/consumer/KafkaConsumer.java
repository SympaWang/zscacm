package com.example.zscacm.consumer;


import com.example.zscacm.entity.CfProblems;
import com.example.zscacm.entity.Message;
import com.example.zscacm.service.CfService;
import com.example.zscacm.service.MessageService;
import com.example.zscacm.utils.RedisCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Component
public class KafkaConsumer {

    @Resource
    private MessageService messageService;

    @Resource
    private CfService cfService;

    @Resource
    private RedisCache redisCache;

    @KafkaListener(topics = {"notice"})
    public void onReply(ConsumerRecord<?, ?> record){
        Message message = (Message) record.value();

        messageService.addMessage(message);

    }

    @KafkaListener(topics = {"cfProblem"})
    public void onCfProblem(ConsumerRecord<?, ?> record){
        HashMap<String, Object> map = (HashMap<String, Object>) record.value();

        ObjectMapper mapper = new ObjectMapper();


        CfProblems problem = mapper.convertValue(map.get("problem"), CfProblems.class);
        System.out.println(problem);
        List<String> types = (List<String>) map.get("types");

        int firstId = problem.getFirstId();
        int secondId = problem.getSecondId();
        int thirdId = problem.getThirdId();
        int dif = problem.getDifficulty();

        CfProblems exist = cfService.selectProblemByIds(firstId, secondId, thirdId);

        redisCache.deleteObject("cfProblem:" + firstId + ":" + secondId + ":" + thirdId);
        if(exist != null) {
            if(exist.getDifficulty() == dif) {
                return;
            }
            cfService.updateDifById(dif, firstId, secondId, thirdId);
        } else {
            cfService.addProblem(problem);
            for(String type : types) {
                cfService.addProblemType(firstId, secondId, thirdId, type);
            }
        }
    }


}
