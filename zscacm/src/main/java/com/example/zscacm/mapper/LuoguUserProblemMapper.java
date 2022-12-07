package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.LuoguUser;
import com.example.zscacm.entity.LuoguUserProblem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LuoguUserProblemMapper extends BaseMapper<LuoguUserProblem> {

    @Select("select id, lgid, problem_type, problem_num from luogu_user_problem where lgid=#{lgid} and problem_type=#{type}")
    public LuoguUserProblem selectProblemByType(int lgid, int type);
}
