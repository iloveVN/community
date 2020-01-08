package com.garen.community.service;

import com.garen.community.domain.User;

public interface UserSerivce {

    public User getUser(Integer id);

    public void saveUser(User user);

    public User getUserByName(String username);
}
