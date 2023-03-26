package com.example.zscacm.consumer;


import com.example.zscacm.entity.Message;
import com.example.zscacm.service.MessageService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class KafkaConsumer {

    @Resource
    private MessageService messageService;

    @KafkaListener(topics = {"notice"})
    public void onMessage1(ConsumerRecord<?, ?> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        Message message = (Message) record.value();

        messageService.addMessage(message);

        System.out.println("简单消费："+record.topic()+"-"+record.partition()+"-"+record.value());
    }

}
