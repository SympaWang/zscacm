package com.example.zscacm.controller;


import com.example.zscacm.entity.Reply;
import com.example.zscacm.entity.SysUser;
import com.example.zscacm.service.ReplyService;
import com.example.zscacm.service.UserService;
import com.example.zscacm.utils.ResponseResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ReplyController {

    @Resource
    private ReplyService replyService;

    @Resource
    private UserService userService;

    @Transactional
    @PostMapping("/submitReply")
    public ResponseResult submitReply(@RequestBody Reply reply) {
        reply.setCreateTime(new Date());

        int result = replyService.submitReply(reply);

        return new ResponseResult(200, "添加评论成功", result);
    }

    @GetMapping("/getReplyList")
    public ResponseResult getReplyList(@RequestParam(value = "discussId") int discussId) {

        List<Reply> replyList = replyService.getReplyList(discussId);

        List<Map<String, Object>> result = new ArrayList<>();

        if(replyList != null && replyList.size() != 0) {
            for(Reply reply : replyList) {
                Map<String, Object> map = new HashMap<>();

                map.put("id", reply.getId());

                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                map.put("createTime", f.format(reply.getCreateTime()));
                map.put("comment", reply.getContext());

                map.put("name", userService.selectNameById(reply.getFromId()));
                map.put("inputShow", false);

                List<Reply> secondReplyList = replyService.getSecondReplyList(reply.getId());

                if(secondReplyList != null) {
                    map.put("commentNum", secondReplyList.size());

                    List<Map<String, Object>> secondResult = new ArrayList<>();
                    for(Reply secondReply : secondReplyList) {
                        Map<String, Object> secondMap = new HashMap<>();
                        secondMap.put("fromId", secondReply.getFromId());
                        secondMap.put("toId", secondReply.getToId());
                        secondMap.put("from", userService.selectNameById(secondReply.getFromId()));
                        secondMap.put("to", userService.selectNameById(secondReply.getToId()));
                        secondMap.put("comment", secondReply.getContext());

                        String time = f.format(secondReply.getCreateTime());
                        secondMap.put("createTime", time);
                        secondMap.put("inputShow", false);

                        secondResult.add(secondMap);
                    }
                    map.put("reply", secondResult);
                }

                result.add(map);
            }
        }


        return new ResponseResult(200, "查询评论成功", result);
    }


}
