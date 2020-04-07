package com.garen.community.controller;

import com.garen.community.common.api.CommonPage;
import com.garen.community.common.api.CommonResult;
import com.garen.community.domain.JpaUser;
import com.garen.community.domain.User;
import com.garen.community.mbg.model.TUser;
import com.garen.community.repository.UserRepository;
import com.garen.community.service.TUserService;
import com.garen.community.service.UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserSerivce userSerivce;

    @Autowired
    private TUserService tUserService;

    @Autowired
    private UserRepository userRepository;

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


    // 使用userRepository.getOne(id)
    // 当我查询一个不存在的id数据时，直接抛出异常，因为它返回的是一个引用，简单点说就是一个代理对象
    // 所以说，如果想无论如何都有一个返回，那么就用findOne,否则使用getOne
    @GetMapping("/jpauser/{id}")
    public JpaUser getJpaUser(@PathVariable("id") Integer id) {
       Optional<JpaUser> jpaUser = userRepository.findById(id);
       return jpaUser.isPresent() ? jpaUser.get() : null;
    }

    @GetMapping("/jpauser")
    public JpaUser insertJpaUser(JpaUser jpaUser) {
        JpaUser save = userRepository.save(jpaUser);
        return save;
    }

 }
