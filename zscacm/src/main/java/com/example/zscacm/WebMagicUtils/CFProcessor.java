package com.example.zscacm.WebMagicUtils;

import com.example.zscacm.entity.CfProblems;
import com.example.zscacm.entity.CfProblemsType;
import com.example.zscacm.mapper.CfProblemsMapper;
import com.example.zscacm.mapper.CfProblemsTypeMapper;
import com.example.zscacm.service.CfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CFProcessor implements PageProcessor {

    private Site site;

    private final Logger logger = LoggerFactory.getLogger(CFProcessor.class);

    @Resource
    private CfService cfService;

    @Override
    @Transactional
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

            String name = names.get(i);

            String difPath = "//*[@id=\"pageContent\"]/div[2]/div[6]/table/tbody/tr[" + (i + 2) + "]/td[4]/span/text()";
            String difString = page.getHtml().xpath(difPath).toString();
            int dif = Integer.parseInt(difString == null ? "0" : difString);
            logger.info("" + dif);

            String acPath = "//*[@id=\"pageContent\"]/div[2]/div[6]/table/tbody/tr[" + (i + 2) + "]/td[5]/a/text()";
            String acString = page.getHtml().xpath(acPath).toString();

            if(acString != null) {
                acString = acString.substring(2);
            }
            logger.info(acString);
            int acceptNum = Integer.parseInt(acString == null ? "0" : acString);


            String typePath = "//*[@id=\"pageContent\"]/div[2]/div[6]/table/tbody/tr[" + (i + 2) + "]/td    [2]/div[2]/a/text()";
            List<String> types = page.getHtml().xpath(typePath).all();
            logger.info(types.toString());

            CfProblems exist = cfService.selectProblemByIds(firstId, secondId, thirdId);
            if(exist != null) {
                continue;
            }

            CfProblems cfProblems = CfProblems.builder().firstId(firstId).secondId(secondId).thirdId(thirdId)
                    .problemName(name).difficulty(dif).url(url).acceptNum(acceptNum).build();
            cfService.addProblem(cfProblems);

            for(String type : types) {
                cfService.addProblemType(firstId, secondId, thirdId, type);
            }
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
