package com.yun.springbootmall.dao;

import com.yun.springbootmall.dto.UserRequest;
import com.yun.springbootmall.model.User;

public interface UserDao {
    public Integer createDao( UserRequest userRequest);

    public User findUserId(Integer id);

    public User getUserEmail(String email);
}
