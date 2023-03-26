package com.example.zscacm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.zscacm.entity.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReplyMapper extends BaseMapper<Reply> {


    @Select("select * from reply where reply_id=#{discussId} " +
            "and reply_type=1 order by create_time desc")
    List<Reply> selectReplyList(int discussId);

    @Select("select * from reply where reply_id=#{replyId} " +
            "and reply_type=2 order by create_time desc")
    List<Reply> selectSecondReplyList(int replyId);
}
