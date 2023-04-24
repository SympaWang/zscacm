package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper  extends BaseMapper<SysUser> {

    @Select("select * from sys_user where grade=#{grade}")
    public List<SysUser> selectUserByGrade(Integer grade);

    @Select("select * from sys_user")
    public List<SysUser> selectUser();

    @Select("select handle from sys_user where username=#{name}")
    public String selectHandleByName(String name);

    @Select("select username from sys_user where handle=#{handle}")
    public String selectNameByHandle(String handle);

    @Select("select id from sys_user where username=#{name}")
    public Integer selectIdByName(String name);

    @Select("select username from sys_user where id=#{id}")
    String selectNameById(int id);


    @Select("select distinct 1 from sys_user")
    int selectHeartbeat();
}
