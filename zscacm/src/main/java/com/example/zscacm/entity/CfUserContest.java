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
public class CfUserContest {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer contestId;

    private String handle;

    private Integer rank;

    private Date ratingTime;

    private Integer oldRating;

    private Integer newRating;
}
