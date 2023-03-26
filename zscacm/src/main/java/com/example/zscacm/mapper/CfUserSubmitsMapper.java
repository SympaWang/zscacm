package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.CfProblemIds;
import com.example.zscacm.entity.CfUserSubmits;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CfUserSubmitsMapper extends BaseMapper<CfUserSubmits> {

    @Select("select * from cf_user_submits " +
            " where uid = #{cfId} and first_id = #{firstId} and second_id = #{secondId} and third_id = #{thirdId}")
    public CfUserSubmits selectSubmitById(int cfId, int firstId, int secondId, int thirdId);

    @Select("update cf_user_submits set status=#{status}, creation_time=#{creationTime}" +
            " where uid = #{cfId} and first_id = #{firstId} and second_id = #{secondId} and third_id = #{thirdId}")
    public Integer updateSubmitById(int cfId, int firstId, int secondId, int thirdId, String status, int creationTime);

    @Select("select * from cf_user_submits where uid = #{cfId} and status!='OK'")
    List<CfUserSubmits> selectUserSubmit(Integer cfId);

    @Select("select first_id, second_id, third_id from cf_user_submits where uid = #{cfId}")
    List<CfProblemIds> selectUserSubmitIds(Integer id);
}
