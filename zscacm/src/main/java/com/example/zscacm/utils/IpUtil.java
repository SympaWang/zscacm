package com.example.zscacm.utils;


import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * ip处理
 * Created by chenpeng on 2022/06/07.
 */

public class IpUtil {
    /**
     * 获取访问用户的客户端IP（适用于公网与局域网）.
     */
    public static String getIpAddr(HttpServletRequest request){
        try{
            if (request == null) {
                //throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
                return "";
            }
            String ipString = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ipString) || "unknown".equalsIgnoreCase(ipString)) {
                ipString = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ipString) || "unknown".equalsIgnoreCase(ipString)) {
                ipString = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ipString) || "unknown".equalsIgnoreCase(ipString)) {
                ipString = request.getRemoteAddr();
            }
            // 多个路由时，取第一个非unknown的ip
            String[] arr = ipString.split(",");
            for (String str : arr) {
                if (!"unknown".equalsIgnoreCase(new StringBuffer(str).toString())) {
                    ipString = str;
                    break;
                }
            }
            if(ipString.equals("0:0:0:0:0:0:0:1")){
                ipString = "127.0.0.1";
            }
            return ipString;
        }catch(Exception e){
            return "";
        }
    }
}