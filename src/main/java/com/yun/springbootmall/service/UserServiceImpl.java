package com.yun.springbootmall.service;

import com.yun.springbootmall.dao.UserDao;
import com.yun.springbootmall.dto.UserRequest;
import com.yun.springbootmall.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService{
    @Autowired
    UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Integer register(UserRequest userRequest) {
        //檢查該 email 是否已存在
        User userEmail = userDao.getUserEmail(userRequest.getEmail());
        if (userEmail != null){
            log.warn("該 email {} 已經被註冊", userRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRequest.getPassword().getBytes());
        userRequest.setPassword(hashedPassword);

        //創建帳號
        return userDao.createDao(userRequest);
    }

    @Override
    public User findUserId(Integer id) {
        return userDao.findUserId(id);
    }

    @Override
    public User login(UserRequest userRequest) {
        User resUser = userDao.login(userRequest);

        //使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRequest.getPassword().getBytes());
        String email = userRequest.getEmail();

        if (resUser == null){
            log.warn("該 email {} 尚未註冊", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String resPassword = resUser.getPassword();
        if (!resPassword.equals(hashedPassword)){
            log.warn("email {} 輸入錯誤，請確認", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return resUser;
    }
}
