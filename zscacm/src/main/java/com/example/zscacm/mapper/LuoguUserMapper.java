package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.LuoguUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LuoguUserMapper extends BaseMapper<LuoguUser> {

    @Select("select uid, lgid, total_problems, add_time from luogu_user where lgid = #{lgid}")
    public LuoguUser selectByLgid(int lgid);

    @Select("select total_problems from luogu_user where lgid = #{lgid}")
    public Integer selectTotalProblemByLgid(int lgid);
}
