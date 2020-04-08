package com.garen.community.domain;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "atguigu", type = "book")
public class Book {

    private Integer id;
    private String bookName;
    private String author;

}
