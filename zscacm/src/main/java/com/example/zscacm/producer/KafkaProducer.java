package com.example.zscacm.producer;

import com.example.zscacm.entity.Message;
import com.example.zscacm.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public void sendMessage1(Message message) {
        System.out.println(123456);
        kafkaTemplate.send("notice", message);
    }

}
