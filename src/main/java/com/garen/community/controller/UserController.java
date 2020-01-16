package com.garen.community.controller;

import com.garen.community.common.api.CommonPage;
import com.garen.community.common.api.CommonResult;
import com.garen.community.domain.User;
import com.garen.community.mbg.model.TUser;
import com.garen.community.service.TUserService;
import com.garen.community.service.UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserSerivce userSerivce;

    @Autowired
    private TUserService tUserService;

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return userSerivce.getUser(id);
    }

    @GetMapping("/updateuserbyid")
    public String updateUserById(User user) {
        userSerivce.updateUserById(user);
        return "success";
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

    @GetMapping("/deluser/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userSerivce.deleteUser(id);
        return "success";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<TUser>> listBrand(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                     @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {
        List<TUser> brandList = tUserService.listUser(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(brandList));
    }

 }
