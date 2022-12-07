package com.example.zscacm;

import com.example.zscacm.processor.LuoguProcessor;
import com.example.zscacm.utils.SeleniumDownloader;
import com.example.zscacm.entity.LuoguUser;
import com.example.zscacm.service.LuoguService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.Spider;

@SpringBootTest(classes = ZscacmApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LuoguTest {

    @Autowired
    private LuoguService luoguService;

    @Autowired
    private LuoguProcessor luoguProcessor;

    @Test
    public void testInsert() {
        int lgid = 262041;
        LuoguUser user = luoguService.selectUserByLgid(lgid);
        if(user == null) {
            int uid = luoguService.addUser(lgid);
        }
        String url = "https://www.luogu.com.cn/user/" + lgid + "#practice";
        Spider spider =  Spider.create(luoguProcessor);
        spider.addUrl(url)
                .setDownloader(new SeleniumDownloader("C:\\Program Files (x86)\\chromedriver.exe"))
                .thread(5)
                .run();
    }
}
