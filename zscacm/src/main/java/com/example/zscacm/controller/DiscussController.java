package com.example.zscacm.controller;

import com.example.zscacm.entity.Discuss;
import com.example.zscacm.entity.SysUser;
import com.example.zscacm.service.DiscussService;
import com.example.zscacm.service.UserService;
import com.example.zscacm.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DiscussController {

    @Autowired
    private DiscussService discussService;

    @Autowired
    private UserService userService;

    @PostMapping("/addDiscuss")
    public ResponseResult addDiscuss(@RequestBody Map<String,String> map) {
        String username = map.get("username");

        Integer id = userService.selectIdByName(username);

        Discuss discuss = Discuss.builder().uid(id).title(map.get("title")).context(map.get("context")).build();

        int addNum = discussService.addDiscuss(discuss);

        if(addNum != 0) {
            return new ResponseResult(200, "添加成功", addNum);
        } else {
            return new ResponseResult(500, "添加失败", null);
        }
    }

    @GetMapping("/discussList")
    public ResponseResult discussList(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                      @RequestParam(value = "limit", required = false, defaultValue = "25") Integer limit) {

        int total = discussService.getDiscussCount();

        List<Discuss> discussList = discussService.getDiscussPage(currentPage, limit);

        List<Map<String, Object>> result = new ArrayList<>();

        for(Discuss discuss : discussList) {
            Map<String, Object> map = new HashMap<>();

            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            SysUser user = userService.selectUserById(discuss.getUid());

            map.put("discussId", discuss.getId());
            map.put("username", user.getUsername());
            map.put("title", discuss.getTitle());
            map.put("uid", discuss.getUid());
            map.put("createTime", f.format(discuss.getCreateTime()));
            map.put("updateTime", f.format(discuss.getUpdateTime()));
            map.put("reply_num", discuss.getReplyNum());
            map.put("context", discuss.getContext());

            result.add(map);
        }

        return new ResponseResult(200, "查询成功", result);

    }
}
