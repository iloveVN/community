package com.garen.community.service.impl;

import com.garen.community.mbg.mapper.TUserMapper;
import com.garen.community.mbg.model.TUser;
import com.garen.community.mbg.model.TUserExample;
import com.garen.community.service.TUserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TUserServiceImpl implements TUserService {

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public List<TUser> listUser(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return tUserMapper.selectByExample(new TUserExample());
    }
}
