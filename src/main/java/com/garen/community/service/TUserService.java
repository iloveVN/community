package com.garen.community.service;


import com.garen.community.mbg.model.TUser;

import java.util.List;

public interface TUserService {


    List<TUser> listUser(int pageNum, int pageSize);

}
