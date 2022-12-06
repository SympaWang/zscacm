package com.example.zscacm;

import com.example.zscacm.WebMagicUtils.CFProcessor;
import com.example.zscacm.WebMagicUtils.SeleniumDownloader;
import com.example.zscacm.mapper.CfProblemsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = ZscacmApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CFTest {

    @Autowired
    private CFProcessor cfProcessor;

    @Resource
    CfProblemsMapper cfProblemsMapper;

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
}
