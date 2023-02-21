package com.example.zscacm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CfContests {

    private Integer id;

    private String name;

    private Integer durationSeconds;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date beginTime;

    private Integer relativeTime;


}
