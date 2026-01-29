package com.yun.springbootmall.service;

import com.yun.springbootmall.dto.UserRequest;
import com.yun.springbootmall.model.User;

public interface UserService {
    public Integer register( UserRequest userRequest);

    public User findUserId(Integer id);

    public User login(UserRequest userRequest);
}
