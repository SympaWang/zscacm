package com.example.zscacm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class CfUserSubmit {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer uid;

    private Integer firstId;

    private Integer secondId;

    private Integer thirdId;

    private Boolean status;
}
