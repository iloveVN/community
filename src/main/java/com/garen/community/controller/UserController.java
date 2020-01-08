package com.garen.community.controller;

import com.garen.community.domain.User;
import com.garen.community.service.UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserSerivce userSerivce;

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return userSerivce.getUser(id);
    }

    @GetMapping("/user")
    public String saveUser(User user) {
        userSerivce.saveUser(user);
        return "success";
    }

    @GetMapping("/userbyname/{username}")
    public User getUserByName(@PathVariable("username") String username) {
        return userSerivce.getUserByName(username);
    }


    @GetMapping("/updateuser")
    public String updateUser(User user) {
        userSerivce.updateUser(user);
        return "success";
    }
 }
