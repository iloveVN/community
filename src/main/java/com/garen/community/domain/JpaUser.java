package com.garen.community.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_user")
public class JpaUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
}
