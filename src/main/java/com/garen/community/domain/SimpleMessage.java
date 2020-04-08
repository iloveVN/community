package com.garen.community.domain;


import lombok.Data;

import java.util.Date;

@Data
public class SimpleMessage {

    private String publisher;
    private String content;
    private Date createtime;

}
