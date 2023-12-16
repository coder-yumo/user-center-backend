package com.yupi.usercenterbackend.service;

import com.yupi.usercenterbackend.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author linli
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-12-15 22:57:04
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount 用户账户
     * @param password 用户密码
     * @param checkPassword 校验密码
     * @return 用户id
     */
    long userRegister(String userAccount,String password,String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount 用户账户
     * @param password 用户密码
     * @return 返回脱敏后的用户信息
     */
    User userLogin(String userAccount, String password, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafe(User originUser);
}
