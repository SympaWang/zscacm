package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.CfContests;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CfContestsMapper extends BaseMapper<CfContests> {

    @Select("select id, name, duration_seconds, begin_time, relative_time from cf_contests where relative_time<=0 order by relative_time desc")
    public List<CfContests> getFutureContests();
}
