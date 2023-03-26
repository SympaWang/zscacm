package com.example.zscacm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.zscacm.entity.Attendance;
import com.example.zscacm.mapper.AttendanceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Wrapper;

@Service
public class AttendanceService {

    @Resource
    private AttendanceMapper attendanceMapper;

    public int insertAttendance(Attendance attendance) {

        return attendanceMapper.insert(attendance);
    }

    public Attendance selectAttendance(int uid, int startTime) {

        QueryWrapper<Attendance> wrapper = new QueryWrapper<>();

        wrapper.eq("uid", uid);
        wrapper.eq("day", startTime);

        return attendanceMapper.selectOne(wrapper);
    }
}
