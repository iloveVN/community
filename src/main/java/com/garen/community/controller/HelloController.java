package com.garen.community.controller;


import com.garen.community.config.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hello")
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

    @ResponseBody
    @PostMapping("/convert")
    public String commonHttpRequestParamConvert(HttpServletRequest req) {

        Map<String, String> params = new HashMap<>();
        try {
            Map<String, String[]> requestParams = req.getParameterMap();
            if (requestParams != null && !requestParams.isEmpty()) {
                requestParams.forEach((key, value) -> params.put(key, value[0]));
            } else {
                StringBuilder paramSb = new StringBuilder();
                try {
                    String str = "";
                    BufferedReader br = req.getReader();
                    while((str = br.readLine()) != null){
                        paramSb.append(str);
                    }
                } catch (Exception e) {
                    System.out.println("httpServletRequest get requestbody error, cause : " + e);
                }
                if (paramSb.length() > 0) {
//                    JSONObject paramJsonObject =
//                    if (paramJsonObject != null && !paramJsonObject.isEmpty()) {
//                        paramJsonObject.forEach((key, value) -> params.put(key, String.valueOf(value)));
//                    }
                }
            }
        } catch (Exception e) {
            System.out.println("commonHttpRequestParamConvert error, cause : " + e);
        }

        return "success";
    }


}
