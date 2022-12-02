package com.example.zscacm.WebMagicUtils;

import com.example.zscacm.service.LuoguService;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class LuoguProcessor implements PageProcessor {

    @Resource
    private LuoguService luoguService;

    private Site site;

    @Override
    public void process(Page page) {
        List<Integer> num = new ArrayList<>();
        String url = page.getUrl().toString();
        url = url.substring(url.indexOf("user/") + 5, url.indexOf('#'));
        int lgid = Integer.parseInt(url);

        String totalProblemsPath = "//*[@id=\"app\"]/div[2]/main/div/div[1]/div[2]/div[2]/div/div[4]/a/span[2]/text()";
        Integer totalProblems = Integer.parseInt(page.getHtml().xpath(totalProblemsPath).toString());
        for(int i = 1; i <= 8; i++) {
            String numPath = "//*[@id=\"app\"]/div[2]/main/div/div[2]/section[1]/div/div[1]/div[" + i + "]/span[2]/text()";
            String numstr = page.getHtml().xpath(numPath).toString();
            numstr = numstr.substring(0, numstr.length() - 1);
            num.add(Integer.parseInt(numstr));
        }
        page.putField("num", num);
        page.putField("totalProblems", totalProblems);

        if (page.getResultItems().get("type") == null) {
            //skip this page
            page.setSkip(true);
        }

        luoguService.updateSolved(lgid, num, totalProblems);
    }

    @Override
    public Site getSite() {
        if(site == null) {
            site = Site.me().setRetrySleepTime(3).setSleepTime(100);
        }
        return site;
    }

}