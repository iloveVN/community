package com.garen.community.mapper;

import com.garen.community.domain.User;

import java.util.List;

public interface UserMapper {

    public User getUser(Integer id);

    public void saveUser(User user);

    public List<User> getUserByName(String username);
}
