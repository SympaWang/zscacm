package com.example.zscacm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CfUserContest implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer contestId;

    private String handle;

    private Integer rank;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date ratingTime;

    private Integer oldRating;

    private Integer newRating;
}
