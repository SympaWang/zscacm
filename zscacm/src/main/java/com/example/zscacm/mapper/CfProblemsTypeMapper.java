package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.CfProblemsType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CfProblemsTypeMapper extends BaseMapper<CfProblemsType> {

    @Select("select type from cf_problems_type " +
            "where first_id=#{firstId} and second_id=#{secondId} and third_id=#{thirdId} and type=#{type}")
    public CfProblemsType selectTypeByIdType(int firstId, int secondId, int thirdId, String type);

    @Select("select type from cf_problems_type " +
            "where first_id=#{firstId} and second_id=#{secondId} and third_id=#{thirdId}")
    public List<String> selectTypeById(int firstId, int secondId, int thirdId);

    @Select("select first_id, second_id, third_id, type from cf_problems_type " +
            "where type=#{type}")
    public List<CfProblemsType> selectTypeByType(String type);

}
