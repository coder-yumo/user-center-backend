package com.yupi.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.usercenterbackend.mapper.UserMapper;
import com.yupi.usercenterbackend.model.User;
import com.yupi.usercenterbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
* @author linli
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-12-15 22:57:04
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{


    @Override
    public long userRegister(String userAccount, String password, String checkPassword) {
        //校验
        if (StringUtils.isAnyBlank(userAccount,password,checkPassword)){
            return -1;
        }
        if (userAccount.length()<4){
            return -1;
        }
        if (password.length()<8 || checkPassword.length()<8){
            return -1;
        }
        //校验账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        if (!password.equals(checkPassword)){
            return -1;
        }

        //用户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUserAccount,userAccount);
        User user = this.getOne(queryWrapper);
        if (user != null){
            return -1;
        }

        //对密码进行加密
        //加盐
        final String SALT = "yupi";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+password).getBytes());
        //插入数据
        User u = new User();
        u.setUserAccount(userAccount);
        u.setUserPassword(encryptPassword);
        boolean saveResult = this.save(u);
        if (!saveResult){
            return -1;
        }
        return u.getId();
    }
}




