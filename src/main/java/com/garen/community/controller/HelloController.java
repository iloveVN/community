package com.garen.community.controller;


import com.garen.community.config.MyException;
import com.mysql.cj.jdbc.JdbcConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HelloController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/hello")
    public String hello(@RequestParam(name="name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

    @RequestMapping("testException")
    public String testException() throws Exception{
        throw new MissingServletRequestParameterException("name","String");
    }

    @RequestMapping("testMyException")
    public String testMyException() throws MyException{
        throw new MyException("i am a myException");
    }

    @GetMapping("/index")
    public String hello() {
        return "index";
    }

    @GetMapping("/user")
    public String user(Model model) throws Exception {
        List<Map<String, Object>> lists = jdbcTemplate.queryForList("select * from users");
        model.addAttribute("user", lists.get(0));
        return "user";
    }

    @GetMapping("/users")
    @ResponseBody
    public Map<String, Object> users() throws Exception {
        List<Map<String, Object>> lists = jdbcTemplate.queryForList("select * from user");
        return lists.get(0);
    }
}
