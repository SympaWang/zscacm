package com.example.zscacm;

import com.example.zscacm.entity.CfContests;
import com.example.zscacm.entity.CfUser;
import com.example.zscacm.entity.CfUserContest;
import com.example.zscacm.processor.CfProblemProcessor;
import com.example.zscacm.processor.CfUserProcessor;
import com.example.zscacm.producer.KafkaProducer;
import com.example.zscacm.service.CfService;
import com.example.zscacm.utils.CfApiUtil;
import com.example.zscacm.utils.SeleniumDownloader;
import com.example.zscacm.mapper.CfProblemsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest(classes = ZscacmApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CFTest {

    @Autowired
    private CfProblemProcessor cfProcessor;

    @Autowired
    private CfUserProcessor cfUserProcessor;

    @Resource
    CfProblemsMapper cfProblemsMapper;

    @Resource
    private CfApiUtil cfApiUtil;

    @Resource
    private CfService cfService;

    @Resource
    private KafkaProducer kafkaProducer;

    @Test
    public void test() {

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

    @Test
    public void testUser() {

        String handle = "Sympa";
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
                .setDownloader(new SeleniumDownloader("C:\\Program Files (x86)\\Tencent\\chromedriver.exe"))
                .thread(5)
                .run();


    }


    @Test
    public void testContest() {
        List<CfContests> list = cfApiUtil.getContestList();
        int addNum = cfService.addContest(list);

        System.out.println("新增了" + addNum + "场比赛");
    }

    @Test
    public void testUserContest() {
        List<CfUserContest> list = cfApiUtil.getUserContest("Sympa");
        int addNum = cfService.addUserContest(list);
        System.out.println(addNum);

    }

    @Test
    public void getFutureContests() {
        List<CfContests> list = cfService.getFutureContests();
        System.out.println(list);
    }

    @Test
    public void getUserSubmit() {
        //List<CfUserSubmit> submits = cfApiUtil.getSubmitList("sympa");
        //System.out.println(submits);
    }

    @Test
    public void getTime() {
        LocalDateTime today_start = LocalDateTime.of(LocalDate.of(2023, 3, 16), LocalTime.MIN);//当天零点
        //获取当天结束时间
        LocalDateTime today_end = LocalDateTime.of(LocalDate.of(2023, 3, 16), LocalTime.MAX);//当天24点

        String td_st_str = today_start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String td_ed_str = today_end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //获取秒数  10位
        long start_time = today_start.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
        long end_time = today_end.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();


        System.out.println(start_time);
        System.out.println(end_time);


    }
}
