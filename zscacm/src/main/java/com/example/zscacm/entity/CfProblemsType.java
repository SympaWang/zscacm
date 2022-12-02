package com.example.zscacm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CfProblemsType {

    private Integer id;

    private Integer firstId;

    private Integer secondId;

    private Integer thirdId;

    private String type;
}
