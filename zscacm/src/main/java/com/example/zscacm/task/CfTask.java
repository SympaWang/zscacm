package com.example.zscacm.task;

import com.example.zscacm.entity.*;
import com.example.zscacm.processor.CfProblemProcessor;
import com.example.zscacm.processor.CfUserProcessor;
import com.example.zscacm.service.CfService;
import com.example.zscacm.service.UserService;
import com.example.zscacm.utils.CfApiUtil;
import com.example.zscacm.utils.SeleniumDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.util.List;

@Component
public class CfTask {

    @Autowired
    private CfService cfService;

    @Autowired
    private CfProblemProcessor cfProcessor;

    @Autowired
    private CfUserProcessor cfUserProcessor;

    @Autowired
    private CfApiUtil cfApiUtil;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 34 13 * * ? ")
    public void CfProblemTask(){
        int total = cfService.selectProblemCount();
        Spider spider =  Spider.create(cfProcessor);

        for(int i = 1; i <= 25; i++) {
            String url = "https://codeforces.com/problemset/page/" + i;
            spider.addUrl(url)
                    .setDownloader(new SeleniumDownloader("C:\\Program Files (x86)\\chromedriver.exe"))
                    .thread(5)
                    .run();
        }
        System.out.println("cf问题添加完毕");
    }

    @Scheduled(cron = "0 21 12 * * ? ")
    public void cfContestTask() {
        List<CfContests> list = cfApiUtil.getContestList();
        int addNum = cfService.addContest(list);
        System.out.println("新增了" + addNum + "场比赛");
    }

    @Scheduled(cron = "0 0 12 * * ? ")
    public void cfUserTask() {
        List<SysUser> userList = userService.selectUser();

        for(SysUser user : userList) {
            String handle = user.getHandle();
            CfUser cfUser = cfService.selectUser(handle);
            if(cfUser == null) {
                cfUser = cfApiUtil.getUserDetail(handle);
                cfService.addUser(cfUser);
            } else {
                cfService.updateUser(cfUser);
            }
            Spider spider =  Spider.create(cfUserProcessor);
            String url = "https://codeforces.com/profile/Sympa";
            spider.addUrl(url)
                    .setDownloader(new SeleniumDownloader("C:\\Program Files (x86)\\chromedriver.exe"))
                    .thread(5)
                    .run();

            List<CfUserContest> list = cfApiUtil.getUserContest(handle);
            int addNum = cfService.addUserContest(list);
            System.out.println(handle + "增加了" + addNum + "条数据");
        }
    }

    @Scheduled(cron = "0 0/50 * * * ? ")
    public void cfUserSubmitTask() {
        List<SysUser> userList = userService.selectUser();
        for(SysUser user : userList) {
            String handle = user.getHandle();
            if(handle == null) continue;

            Integer cfId = cfService.selectUserId(handle);

            for(int page = 1; page <= 5; page++) {
                int from = (page - 1) * 25 + 1;
                String url = "https://codeforces.com/api/user.status?handle=" + handle + "&from=" + from
                        + "&count=25";
                List<CfUserSubmits> submits = cfApiUtil.getSubmitList(url, handle);
                if(submits.size() == 0) break;
                for(CfUserSubmits submit : submits) {
                    CfUserSubmits userSubmit = cfService.selectSubmitById(cfId, submit.getFirstId(),
                            submit.getSecondId(), submit.getThirdId());
                    if(userSubmit == null) {
                        cfService.insertUserSubmit(submit);
                    } else if(submit.getCreationTime() < userSubmit.getCreationTime() && !userSubmit.getStatus().equals("OK")) {
                        cfService.updateSubmitById(cfId, submit.getFirstId(), submit.getSecondId(),submit.getThirdId(),
                                submit.getStatus(), submit.getCreationTime());
                    }
                }
                System.out.println("第" + page + "页添加完成");
            }
        }

    }
}
