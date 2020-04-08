package com.garen.community.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(description = "用户实体类")
public class User implements Serializable {
    @ApiModelProperty(value = "ID", example = "100")
    private Integer id;
    @ApiModelProperty(value = "姓名", example = "张良")
    private String username;
    @ApiModelProperty(value = "生日")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;
    @ApiModelProperty(value = "性别", example = "1")
    private Integer sex;
    @ApiModelProperty(value = "地址", example = "南京")
    private String address;
}
