package com.example.zscacm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zscacm.entity.Discuss;
import com.example.zscacm.entity.Message;
import com.example.zscacm.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageService {

    @Resource
    private MessageMapper messageMapper;

    public int addMessage(Message message) {
        return messageMapper.insert(message);
    }

    public List<Message> getMessageList(int toId, int pageNum, int limit) {

        Page<Message> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(limit);

        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("to_id", toId).orderByDesc("create_time");

        return messageMapper.selectPage(page, wrapper).getRecords();


    }


    public int getMessageCount() {
        return Math.toIntExact(messageMapper.selectCount(new QueryWrapper<>()));
    }

}
