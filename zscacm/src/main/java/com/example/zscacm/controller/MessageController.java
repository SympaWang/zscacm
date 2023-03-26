package com.example.zscacm.controller;

import com.example.zscacm.entity.Message;
import com.example.zscacm.service.MessageService;
import com.example.zscacm.service.UserService;
import com.example.zscacm.utils.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MessageController{

    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;

    @GetMapping("/getMessage")
    public ResponseResult getMessage(@RequestParam(value = "userId") Integer userId,
                                     @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                     @RequestParam(value = "limit", required = false, defaultValue = "25") Integer limit) {

        int total = messageService.getMessageCount();

        List<Message> messageList = messageService.getMessageList(userId, currentPage, limit);

        List<Map<String, Object>> result = new ArrayList<>();

        for(Message message : messageList) {
            Map<String, Object> map = new HashMap<>();

            String username = userService.selectNameById(message.getFromId());
            map.put("username", username);
            map.put("context", "回复了你的帖子");
            map.put("discussId", message.getTargetId());
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            map.put("createTime", f.format(message.getCreateTime()));

            result.add(map);

        }

        return new ResponseResult(200, "获取消息成功", result);

    }

}
