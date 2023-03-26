package com.example.zscacm.processor;

import com.example.zscacm.entity.SysUser;
import com.example.zscacm.entity.VjUser;
import com.example.zscacm.service.LuoguService;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class VJProcessor implements PageProcessor {

    @Resource
    private LuoguService luoguService;

    private Site site;

    @Override
    public void process(Page page) {

        String vjName = page.getUrl().toString();
        vjName  = vjName .substring(vjName.indexOf("user/") + 5);
        int num = 0;

        String weekProblemsPath = "/html/body/div[1]/div[2]/div[3]/table/tbody/tr[2]/td/a/text()";
        Integer weekProblems = Integer.parseInt(page.getHtml().xpath(weekProblemsPath).toString());

        String monthProblemsPath = "/html/body/div[1]/div[2]/div[3]/table/tbody/tr[3]/td/a/text()";
        Integer monthProblems = Integer.parseInt(page.getHtml().xpath(monthProblemsPath).toString());

        String totalProblemsPath = "/html/body/div[1]/div[2]/div[3]/table/tbody/tr[4]/td/a/text()";
        Integer totalProblems = Integer.parseInt(page.getHtml().xpath(totalProblemsPath).toString());

        VjUser VjUser = com.example.zscacm.entity.VjUser.builder().VjName(vjName).weekProblem(weekProblems).monthProblem(monthProblems)
                .totalProblem(totalProblems).addTime(new Date()).build();

        //luoguService.updateSolved(vjName, weekProblems, monthProblems, totalProblems);
    }

    @Override
    public Site getSite() {
        if(site == null) {
            site = Site.me().setRetrySleepTime(3).setSleepTime(100);
        }
        return site;
    }
}
