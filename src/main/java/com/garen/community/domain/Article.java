package com.garen.community.domain;

import io.searchbox.annotations.JestId;
import lombok.Data;

@Data
public class Article {

    @JestId
    private Integer id;
    private String title;
    private String author;
    private String content;

}
