package com.yupi.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.usercenterbackend.constant.UserConstant;
import com.yupi.usercenterbackend.mapper.UserMapper;
import com.yupi.usercenterbackend.model.User;
import com.yupi.usercenterbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
* @author linli
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-12-15 22:57:04
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    private static final String SALT = "yupi";


    @Override
    public long userRegister(String userAccount, String password, String checkPassword) {
        //校验
        if (StringUtils.isAnyBlank(userAccount,password,checkPassword)){
            return -1;
        }
        if (userAccount.length() < 4){
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

    @Override
    public User userLogin(String userAccount, String password, HttpServletRequest request) {
        //校验
        if (StringUtils.isAnyBlank(userAccount,password)){
            return null;
        }
        if (userAccount.length() < 4){
            return null;
        }
        if (password.length() < 8){
            return null;
        }
         //校验账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
          Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        //对密码进行加密
        //加盐
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+password).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(User::getUserAccount,userAccount)
                .eq(User::getUserPassword,encryptPassword);
        User user = this.getOne(queryWrapper);
        //用户不存在
        if (user == null){
            log.info("user login failed,userAccount cannot match userPassword");
            return null ;
        }

        //用户脱敏
        User safetyUser = getSafe(user);

        //记录用户的登录状态
        HttpSession session = request.getSession();
        session.setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafe(User originUser){
        if (originUser == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        return safetyUser;
    }
}




