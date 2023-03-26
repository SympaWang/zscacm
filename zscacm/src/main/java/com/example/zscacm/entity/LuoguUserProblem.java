package com.example.zscacm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LuoguUserProblem implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer lgid;

    private Integer problemType;

    private Integer problemNum;

    private Date addTime;
}
