package com.example.zscacm.utils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component		// 声明该类被spring管理
@ConfigurationProperties(prefix = "cos")
@Setter     // 注意：必须有setter方法才能赋值成功
@Slf4j
public class CosPropertiesConstantsUtils implements InitializingBean {
    private String secretId;
    private String secretKey;
    private String bucketName;
    private String region;

    public static String Tencent_secretId;
    public static String Tencent_secretKey;
    public static String Tencent_bucketName;
    public static String Tencent_region;

    @Override
    public void afterPropertiesSet() throws Exception {
        Tencent_secretId = secretId;
        Tencent_secretKey = secretKey;
        Tencent_bucketName = bucketName;
        Tencent_region = region;
        log.info("密钥初始化成功");
    }
}
