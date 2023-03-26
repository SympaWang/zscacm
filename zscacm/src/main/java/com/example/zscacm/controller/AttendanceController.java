package com.example.zscacm.controller;

import com.example.zscacm.entity.Attendance;
import com.example.zscacm.entity.SysUser;
import com.example.zscacm.service.AttendanceService;
import com.example.zscacm.service.UserService;
import com.example.zscacm.utils.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class AttendanceController {

    @Resource
    private AttendanceService attendanceService;

    @Resource
    private UserService userService;

    @GetMapping("getAttendance")
    public ResponseResult getAttendance(@RequestParam(value = "date", required = false) String date,
                                        @RequestParam(value = "grade", required = false) Integer grade) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date day = sdf.parse(date);

        LocalDateTime today_start = LocalDateTime.of(LocalDate.of(day.getYear(), day.getMonth(), day.getDay()), LocalTime.MIN);//当天零点
        int start_time = (int) today_start.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();

        List<SysUser> userList;

        if(grade == null) {
            userList = userService.selectUser();
        } else {
            userList = userService.selectUserByGrade(grade);
        }

        List<Attendance> result = new ArrayList<>();

        if(userList != null) {
            for(SysUser user : userList) {
                Attendance attendance = attendanceService.selectAttendance(user.getId(), start_time);
                if(attendance != null) {
                    result.add(attendance);
                }
            }
        }

        return new ResponseResult<>(200, "查找成功", result);
    }

}
