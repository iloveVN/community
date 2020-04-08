package com.garen.community.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 使用JPA注解配置映射关系
 */
@Data
@NoArgsConstructor
@Entity  // 告诉JSP这是一个实体类(和数据库表映射的类)
@Table(name = "tbl_user")  // 指定和哪个数据表对应, 如果省略，默认就是类名小写jpauser
public class JpaUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private Integer id;

    @Column(length = 50) // 这是和数据表对应的一个列
    private String name;
    @Column // 省略，默认列名就是属性名
    private String email;

}