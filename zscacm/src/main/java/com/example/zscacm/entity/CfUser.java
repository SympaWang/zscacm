package com.example.zscacm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CfUser implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String handle;

    private Integer rating;

    private Integer maxRating;

    private String rank;

    private String maxRank;

    private String headerUrl;

    private Integer solvedNum;

}
