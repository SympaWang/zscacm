package com.example.zscacm.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer fromId;

    private Integer toId;

    private Integer replyId;

    private String context;

    private Integer replyType;

    private Date createTime;

}
