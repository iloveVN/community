package com.garen.community.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserVo {

    @NotNull
    private Integer id;

}
