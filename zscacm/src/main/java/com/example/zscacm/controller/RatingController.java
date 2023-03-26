package com.example.zscacm.controller;

import com.example.zscacm.entity.CfUserContest;
import com.example.zscacm.service.CfService;
import com.example.zscacm.service.UserService;
import com.example.zscacm.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class RatingController {

    @Autowired
    private UserService userService;

    @Autowired
    private CfService cfService;

    @GetMapping("/rating/user")
    public ResponseResult getUserRating(@RequestParam(value = "name", required = false) String name) {

        String handle = userService.selectHandleByName(name);

        List<CfUserContest> contestList = cfService.selectUserContestsByHandle(handle);

        List<Map<String, Object>> contests = new ArrayList<>();

        for(CfUserContest contest : contestList) {
            Map<String, Object> map = new HashMap<>();

            String contestName = cfService.selectContestNameById(contest.getContestId());
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String ratingTime = f.format(contest.getRatingTime());
            Integer rank = contest.getRank();
            Integer oldRating = contest.getOldRating();
            Integer newRating = contest.getNewRating();
            Integer change = newRating - oldRating;

            map.put("contestName", contestName);
            map.put("ratingTime", ratingTime);
            map.put("rank", rank);
            map.put("oldRating", oldRating);
            map.put("newRating", newRating);
            map.put("change", change);
            contests.add(map);
        }

        return new ResponseResult(200, "查询成功", contests);
    }

    @GetMapping("/rating/contest")
    public ResponseResult getContestUser(@RequestParam(value = "contestId", required = false) Integer contestId) {

        List<CfUserContest> userContestList = cfService.selectContestUsersById(contestId);

        List<Map<String, Object>> contests = new ArrayList<>();

        for(CfUserContest userContest : userContestList) {
            Map<String, Object> map = new HashMap<>();

            String handle = userContest.getHandle();
            String username = userService.selectNameByHandle(handle);

            Integer rank = userContest.getRank();
            Integer oldRating = userContest.getOldRating();
            Integer newRating = userContest.getNewRating();
            Integer change = newRating - oldRating;

            map.put("handle", handle);
            map.put("username", username);
            map.put("rank", rank);
            map.put("oldRating", oldRating);
            map.put("newRating", newRating);
            map.put("change", change);
            contests.add(map);

        }

        return new ResponseResult(200, "查询成功", contests);

    }
}
