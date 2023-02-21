package com.example.zscacm.utils;

import com.alibaba.fastjson.JSON;
import com.example.zscacm.entity.CfContests;
import com.example.zscacm.entity.CfUser;
import com.example.zscacm.entity.CfUserContest;
import com.example.zscacm.entity.CfUserSubmit;
import com.example.zscacm.service.CfService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CfApiUtil {

    @Resource
    private CfService cfService;

    //通过用户名获取用户信息，返回
    public CfUser getUserDetail(String handle) {
        String url = "https://codeforces.com/api/user.info?handles=" + handle;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).get().build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body() != null ? response.body().string() : null;

            if(str == null) {
                throw new RuntimeException("请求失败！");
            }

            String status = JSON.parseObject(str).get("status").toString();
            if(status.equals("FAILED")) {
                throw new RuntimeException("用户不存在！");
            }

            String result = JSON.parseObject(str).get("result").toString();
            List<HashMap> list = JSON.parseArray(result,HashMap.class);

            HashMap info = list.get(0);
            int rating = (int) info.get("rating");
            int maxRating = (int) info.get("maxRating");
            String rank = (String) info.get("rank");
            String maxRank = (String) info.get("maxRank");
            String headerUrl = (String) info.get("titlePhoto");

            CfUser cfUser = CfUser.builder().handle(handle).rating(rating).maxRating(maxRating).
                    rank(rank).maxRank(maxRank).headerUrl(headerUrl).build();

            return cfUser;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //获取比赛列表
    public List<CfContests> getContestList() {
        String url = "https://codeforces.com/api/contest.list";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).get().build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body() != null ? response.body().string() : null;

            if(str == null) {
                throw new RuntimeException("请求失败！");
            }

            String status = JSON.parseObject(str).get("status").toString();
            if(!status.equals("OK")) {
                throw new RuntimeException("获取比赛列表失败！");
            }

            String result = JSON.parseObject(str).get("result").toString();
            List<HashMap> list = JSON.parseArray(result,HashMap.class);
            List<CfContests> contestList = new ArrayList<>();

            for(HashMap info : list) {
                int id = (int) info.get("id");
                String name = (String) info.get("name");
                int durationSeconds = (int) info.get("durationSeconds");
                int startSeconds = (int) info.get("startTimeSeconds");
                int relativeSeconds = (int) info.get("relativeTimeSeconds");

                Date beginTime = null;
                String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                        .format(Long.parseLong(String.valueOf(startSeconds)) * 1000);
                SimpleDateFormat formater = new SimpleDateFormat();
                formater.applyPattern("yyyy-MM-dd HH:mm:ss");
                try {
                    beginTime = formater.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                CfContests cfContests = CfContests.builder().id(id).name(name).durationSeconds(durationSeconds).
                        beginTime(beginTime).relativeTime(relativeSeconds).build();
                contestList.add(cfContests);
            }

            return contestList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //获取用户提交
    public List<CfUserSubmit> getSubmitList(String handle) {
        String url = "https://codeforces.com/api/user.status?handle=" + handle;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();

        try (Response response = client.newCall(request).execute()) {
            String str = response.body() != null ? response.body().string() : null;

            if(str == null) {
                throw new RuntimeException("请求失败！");
            }

            String status = JSON.parseObject(str).get("status").toString();
            if(!status.equals("OK")) {
                throw new RuntimeException("获取提交列表失败！");
            }

            String result = JSON.parseObject(str).get("result").toString();

            List<HashMap> list = JSON.parseArray(result,HashMap.class);

            List<CfUserSubmit> userSubmits = new ArrayList<>();

            for(HashMap submit : list) {
                HashMap problem = JSON.parseObject(submit.get("problem").toString(), HashMap.class);

                int firstId = (int) problem.get("contestId");
                String index = problem.get("index").toString();
                int secondId = index.charAt(0) - 'A' + 1;
                int thirdId = index.length() == 1 ? 0 : index.charAt(1);
                String verdict = submit.get("verdict").toString();
                boolean subStatus = verdict.equals("OK");

                int id = cfService.selectUserId(handle);
                System.out.println(handle);


                CfUserSubmit userSubmit = CfUserSubmit.builder().uid(id).firstId(firstId).
                        secondId(secondId).thirdId(thirdId).status(subStatus).build();
                userSubmits.add(userSubmit);
            }

            return userSubmits;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //获取用户比赛列表
    public List<CfUserContest> getUserContest(String handle) {
        String url = "https://codeforces.com/api/user.rating?handle=" + handle;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body() != null ? response.body().string() : null;

            if(str == null) {
                throw new RuntimeException("请求失败！");
            }

            String status = JSON.parseObject(str).get("status").toString();
            if(!status.equals("OK")) {
                throw new RuntimeException("获取用户比赛列表失败！");
            }

            String result = JSON.parseObject(str).get("result").toString();

            List<HashMap> list = JSON.parseArray(result,HashMap.class);

            List<CfUserContest> userContests = new ArrayList<>();
            for(HashMap contest : list) {
                int contestId = (int)contest.get("contestId");
                int rank = (int)contest.get("rank");
                int ratingUpdateTimeSeconds = (int)contest.get("ratingUpdateTimeSeconds");
                int oldRating = (int)contest.get("oldRating");
                int newRating = (int)contest.get("newRating");

                Date ratingTime = null;
                String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                        .format(Long.parseLong(String.valueOf(ratingUpdateTimeSeconds)) * 1000);
                SimpleDateFormat formater = new SimpleDateFormat();
                formater.applyPattern("yyyy-MM-dd HH:mm:ss");
                try {
                    ratingTime = formater.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                CfUserContest userContest = CfUserContest.builder().contestId(contestId).rank(rank).ratingTime(ratingTime)
                        .handle(handle).oldRating(oldRating).newRating(newRating).build();
                userContests.add(userContest);
            }
            return userContests;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
