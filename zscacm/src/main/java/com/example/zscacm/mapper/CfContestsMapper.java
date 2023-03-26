package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.CfContests;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface CfContestsMapper extends BaseMapper<CfContests> {

    @Select("select id, name, duration_seconds, begin_time from cf_contests where begin_time>=#{date} order by begin_time")
    public List<CfContests> getFutureContests(String date);

    @Select("select name from cf_contests where id=#{id}")
    public String selectContestNameById(Integer id);

    @Select("select * from cf_contests where relative_time>0 order by begin_time desc")
    public List<CfContests> selectEndContestList();

    @Update("update cf_contests set relative_time=#{relativeTime} where id=#{id}")
    public Integer updateTimeById(Integer relativeTime, Integer id);
}
