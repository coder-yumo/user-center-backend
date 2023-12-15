package com.yupi.usercenterbackend.service;

import com.yupi.usercenterbackend.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author linli
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-12-15 22:57:04
*/
public interface UserService extends IService<User> {

    /**
     * 用户注释
     * @param userAccount 用户账户
     * @param password 用户密码
     * @param checkPassword 校验密码
     * @return 用户id
     */
    long userRegister(String userAccount,String password,String checkPassword);
}
