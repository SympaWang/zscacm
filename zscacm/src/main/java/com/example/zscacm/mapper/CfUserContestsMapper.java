package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.CfUserContest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CfUserContestsMapper extends BaseMapper<CfUserContest> {

    @Select("select * from cf_user_contest where handle=#{handle} order by rating_time desc")
    public List<CfUserContest> selectUserContestsByHandle(String handle);

    @Select("select * from cf_user_contest where contest_id=#{contestId} order by rating_time desc")
    public List<CfUserContest> selectContestUsersById(Integer contestId);

    @Select("select count(id) from cf_user_contest where handle=#{handle}")
    Integer selectUserContestNum(String handle);


    @Select("select * from cf_user_contest where contest_id=#{contestId} and handle=#{handle}")
    CfUserContest selectByUser(Integer contestId, String handle);
}
