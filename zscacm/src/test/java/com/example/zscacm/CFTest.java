package com.example.zscacm;

import com.example.zscacm.processor.CfProblemProcessor;
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

@SpringBootTest(classes = ZscacmApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CFTest {

    @Autowired
    private CfProblemProcessor cfProcessor;

    @Resource
    CfProblemsMapper cfProblemsMapper;

    @Resource
    private CfApiUtil cfApiUtil;

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
    public void testSubmits() {
        System.out.println(cfApiUtil.getSubmitList("Sympa"));

    }
}
