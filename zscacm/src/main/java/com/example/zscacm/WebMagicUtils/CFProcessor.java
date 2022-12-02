package com.example.zscacm.WebMagicUtils;

import com.example.zscacm.entity.CfProblems;
import com.example.zscacm.mapper.CfProblemsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CFProcessor implements PageProcessor {

    private Site site;

    private Logger logger = LoggerFactory.getLogger(CFProcessor.class);

    @Resource
    private CfProblemsMapper cfProblemsMapper;

    @Override
    public void process(Page page) {
        String idPath = "//*[@id=\"pageContent\"]/div[2]/div[6]/table/tbody/tr/td[1]/a/text()";
        List<String> ids = page.getHtml().xpath(idPath).all();
        logger.info(ids.toString());

        String urlPath = "//*[@id=\"pageContent\"]/div[2]/div[6]/table/tbody/tr/td[1]/a/@href";
        List<String> urls = page.getHtml().xpath(urlPath).all();
        logger.info(urls.toString());

        String namePath = "//*[@id=\"pageContent\"]/div[2]/div[6]/table/tbody/tr/td[2]/div[1]/a/text()";
        List<String> names = page.getHtml().xpath(namePath).all();
        logger.info(names.toString());

        String difPath = "//*[@id=\"pageContent\"]/div[2]/div[6]/table/tbody/tr/td[4]/span/text()";
        List<String> difs = page.getHtml().xpath(difPath).all();
        logger.info(difs.toString());

        String acPath = "//*[@id=\"pageContent\"]/div[2]/div[6]/table/tbody/tr/td[5]/a/text()";
        List<String> acs = page.getHtml().xpath(acPath).all();
        logger.info(acs.toString());

        for(int i = 0; i < ids.size(); i++) {

            String id = ids.get(i).replace(" ", "");;
            int firstId = 0;
            int secondId = 0;
            int thirdId = 0;
            for(int j = 0, flag = 0; j < id.length(); j++) {
                if(flag == 0 && id.charAt(j) >= '0' && id.charAt(j) <= '9') {
                    firstId = firstId * 10 + (id.charAt(j) - '0');
                } else if(flag == 1) {
                    thirdId = id.charAt(j) - '0';
                } else {
                    secondId = id.charAt(j) - 'A' + 1;
                    flag = 1;
                }
            }

            String url = "https://codeforces.com" + urls.get(i);

            String name = names.get(i).replace(" ", "");

            int dif = Integer.parseInt(difs.get(i));

            String ac = acs.get(i);
            int acceptNum = Integer.parseInt(ac.substring(ac.indexOf("x") + 1));

            String typePath = "//*[@id=\"pageContent\"]/div[2]/div[6]/table/tbody/tr[" + (i + 2) + "]/td[2]/div[2]/a/text()";
            List<String> types = page.getHtml().xpath(typePath).all();
            logger.info(types.toString());

            CfProblems cfProblems = CfProblems.builder().firstId(firstId).secondId(secondId).thirdId(thirdId)
                    .problemName(name).difficulty(dif).url(url).acceptNum(acceptNum).build();



        }

    }

    @Override
    public Site getSite() {
        if(site == null) {
            site = Site.me().setRetrySleepTime(3).setSleepTime(100).setRetryTimes(5).setTimeOut(20000);
        }
        return site;
    }
}
