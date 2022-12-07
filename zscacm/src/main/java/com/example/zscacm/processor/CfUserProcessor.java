package com.example.zscacm.processor;

import com.example.zscacm.entity.CfUser;
import com.example.zscacm.service.CfService;
import com.example.zscacm.utils.CfApiUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;

@Component
public class CfUserProcessor implements PageProcessor {

    private Site site;

    @Resource
    private CfService cfService;

    @Resource
    private CfApiUtil cfApiUtil;

    @Override
    public void process(Page page) {

        String url = page.getUrl().toString();
        String handle = url.substring(url.indexOf("profile/") + 8);
        CfUser user = cfService.selectUser(handle);
        if(user == null) {
            throw new RuntimeException("用户不存在！");
        }

        user = cfApiUtil.getUserDetail(handle);
        String numPath = "//*[@id=\"pageContent\"]/div[4]/div/div[7]/div[1]/div[1]/div[1]/text()";
        String num = page.getHtml().xpath(numPath).toString(); //xxx problems
        num = num.substring(0, num.indexOf(" "));
        user.setSolvedNum(Integer.parseInt(num));
        cfService.updateUser(user);
    }

    @Override
    public Site getSite() {
        if(site == null) {
            site = Site.me().setRetrySleepTime(3).setSleepTime(100).setRetryTimes(5).setTimeOut(20000);
        }
        return site;
    }
}
