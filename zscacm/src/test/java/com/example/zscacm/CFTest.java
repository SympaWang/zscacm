package com.example.zscacm;

import com.example.zscacm.entity.CfContests;
import com.example.zscacm.entity.CfUser;
import com.example.zscacm.entity.CfUserContest;
import com.example.zscacm.processor.CfProblemProcessor;
import com.example.zscacm.processor.CfSubmitProcessor;
import com.example.zscacm.processor.CfUserProcessor;
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

    @Test
    public void test() {

        int total = cfProblemsMapper.selectCount();
        Spider spider =  Spider.create(cfProcessor);

        for(int i = 1; i <= 83; i++) {
            String url = "https://codeforces.com/problemset/page/" + i;
            spider.addUrl(url)
                    .setDownloader(new SeleniumDownloader("C:\\Program Files (x86)\\chromedriver.exe"))
                    .thread(5)
                    .run();

            int total_now = cfProblemsMapper.selectCount();
            if(total == total_now) {
                System.out.println("第" + i + "页未添加");
                break;
            } else {
                total = total_now;
            }
        }
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
                .setDownloader(new SeleniumDownloader("C:\\Program Files (x86)\\chromedriver.exe"))
                .thread(5)
                .run();


    }


    @Test
    public void testContest() {
        List<CfContests> list = cfApiUtil.getContestList();
        int addNum = cfService.addContest(list);
        System.out.println(addNum);
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
}
