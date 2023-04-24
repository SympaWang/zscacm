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
public class SysUser implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String username;

    private String pyName;

    private String studentNum;

    private String password;

    private Integer lgid;

    private String handle;

    private String vjName;

    private String email;

    private String phone;

    private int sex;

    private int grade;

    private int userType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
