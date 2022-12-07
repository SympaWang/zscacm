package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.CfUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CfUserMapper extends BaseMapper<CfUser> {

    @Select("select id, handle, rating, max_rating, rank, max_rank, header_url from cf_user where handle=#{handle}")
    public CfUser selectUserByHandle(String handle);

    @Select("select id from cf_user where handle=#{handle}")
    public Integer selectUserIdByHandle(String handle);
}
