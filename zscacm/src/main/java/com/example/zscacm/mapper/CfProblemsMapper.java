package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.CfProblems;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CfProblemsMapper extends BaseMapper<CfProblems> {

    @Select("select id, first_id, second_id, third_id, problem_name, difficulty, url, accept_num from cf_problems " +
            "where first_id = #{firstId} and second_id = #{secondId} and third_id = #{thirdId}")
    public CfProblems selectByIds(int firstId, int secondId, int thirdId);

    @Select("select count(id) from cf_problems")
    public int selectCount();

    @Select("select id, first_id, second_id, third_id, problem_name, difficulty, url, accept_num from cf_problems")
    public List<CfProblems> selectList();

    @Update("update cf_problems set difficulty=#{dif} " +
            "where first_id = #{firstId} and second_id = #{secondId} and third_id = #{thirdId}")
    public Integer updateDifById(int dif, int firstId, int secondId, int thirdId);
}
