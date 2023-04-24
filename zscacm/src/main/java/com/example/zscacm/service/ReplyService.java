package com.example.zscacm.service;

import com.example.zscacm.entity.Message;
import com.example.zscacm.entity.Reply;
import com.example.zscacm.mapper.ReplyMapper;
import com.example.zscacm.producer.KafkaProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReplyService {

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private KafkaProducer kafkaProducer;

    @Transactional
    public int submitReply(Reply reply) {
        int result = replyMapper.insert(reply);

        if(result != 0) {

            Message message = Message.builder().fromId(reply.getFromId())
                    .toId(reply.getToId()).targetId(reply.getReplyId()).createTime(reply.getCreateTime()).status(1).type(1).build();

            kafkaProducer.sendReply(message);
        }

        return result;
    }

    public List<Reply> getReplyList(int discussId) {

        return replyMapper.selectReplyList(discussId);
    }

    public List<Reply> getSecondReplyList(int replyId) {

        return replyMapper.selectSecondReplyList(replyId);
    }

}
