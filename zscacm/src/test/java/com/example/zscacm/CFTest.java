package com.example.zscacm;

import com.example.zscacm.WebMagicUtils.CFProcessor;
import com.example.zscacm.WebMagicUtils.SeleniumDownloader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = ZscacmApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CFTest {

    @Autowired
    private CFProcessor cfProcessor;

    @Test
    public void test() {

        List<String> urls = new ArrayList<>();
        for(int i = 1; i <= 83; i++) {
            String url = "https://codeforces.com/problemset/page/" + i;
            urls.add(url);
        }
        String[] urlArray = (String[]) urls.toArray();
        Spider spider =  Spider.create(cfProcessor);
        spider.addUrl(urlArray)
                .setDownloader(new SeleniumDownloader("C:\\Program Files (x86)\\chromedriver.exe"))
                .thread(5)
                .run();
    }
}
