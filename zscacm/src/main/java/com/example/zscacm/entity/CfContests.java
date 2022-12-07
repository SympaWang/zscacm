package com.example.zscacm.entity;

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

    private int durationSeconds;

    private Date beginTime;

    private long relativeTime;


}
