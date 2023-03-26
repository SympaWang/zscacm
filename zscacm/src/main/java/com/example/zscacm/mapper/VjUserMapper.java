package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.SysUser;
import com.example.zscacm.entity.VjUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VjUserMapper extends BaseMapper<VjUser> {

    @Select("select * from vj_user where vj_name=#{vjName}")
    public VjUser selectVjUserByName(String vjName);

    @Select("select total_problem from vj_user where vj_name=#{vjName}")
    public Integer selectTotalProblemByName(String vjName);
}
