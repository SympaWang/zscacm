package com.example.zscacm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.zscacm.entity.Attendance;
import com.example.zscacm.entity.CfUser;
import com.example.zscacm.entity.SysUser;
import com.example.zscacm.service.AttendanceService;
import com.example.zscacm.service.UserService;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(classes = ZscacmApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTest {

    @Resource
    private UserService userService;

    @Resource
    AttendanceService attendanceService;

    @Test
    public void kqTest() {


        List<SysUser> userList = userService.selectUser();

        for(SysUser user : userList) {

            String username = user.getPyName();


            String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ww16aa84a335867100&corpsecret=UafZAssYlzzCRIdW9umo5b29A55mlet5Fmj_muXe710";
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(url).get().build();
            try (Response response = client.newCall(request).execute()) {
                String str = response.body() != null ? response.body().string() : null;

                if(str == null) {
                    throw new RuntimeException("请求失败！");
                }

                int status = Integer.parseInt(JSON.parseObject(str).get("errcode").toString());
                if(status != 0) {
                    throw new RuntimeException("请求失败！");
                }

                String access_token = JSON.parseObject(str).get("access_token").toString();

                url = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token=" + access_token + "&debug=1";


                LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);//当天零点
                //获取当天结束时间
                LocalDateTime today_end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);//当天24点

                String td_st_str = today_start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String td_ed_str = today_end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                //获取秒数  10位
                long start_time = today_start.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
                long end_time = today_end.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("opencheckindatatype", 1);
                jsonObject.put("starttime", start_time);
                jsonObject.put("endtime", end_time);
                jsonObject.put("useridlist", username);

                RequestBody formBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonObject.toString());

                Request request1 = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                Response response1 = client.newCall(request1).execute();
                String str1 = response1.body() != null ? response1.body().string() : null;
                System.out.println(str1);

                String result = JSON.parseObject(str1).get("checkindata").toString();

                List<HashMap> list = JSON.parseArray(result,HashMap.class);

                if(list != null || list.size() != 0) {

                    long beginTime = 1000L *  (int) list.get(0).get("checkin_time");
                    long endTime = 1000L *  (int) list.get(1).get("checkin_time");
                    String status1 = (String) list.get(0).get("exception_type");
                    String status2 = (String) list.get(1).get("exception_type");

                    if(status1.isBlank()) {
                        status1 = "正常";
                    }

                    Date begin = new Date(beginTime);
                    Date end = new Date(endTime);

                    Attendance attendance = Attendance.builder().uid(user.getId()).startTime(begin)
                            .endTime(end).day((int) start_time).status(status1).build();

                    attendanceService.insertAttendance(attendance);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @Test
    public void time() {
        System.out.println(LocalDate.now());
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);//当天零点
        //获取当天结束时间
        LocalDateTime today_end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);//当天24点

        String td_st_str = today_start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String td_ed_str = today_end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //获取秒数  10位
        long start_time = today_start.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
        long end_time = today_end.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
        System.out.println(start_time);
    }
}
