package com.example.zscacm.producer;

import com.example.zscacm.entity.CfProblems;
import com.example.zscacm.entity.Message;
import com.example.zscacm.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public void sendReply(Message message) {
        kafkaTemplate.send("notice", message);
    }

    public void sendCfProblem(HashMap<String, Object> map) {
        kafkaTemplate.send("cfProblem", map);
    }

}
