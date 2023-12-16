package com.yupi.usercenterbackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.usercenterbackend.constant.UserConstant;
import com.yupi.usercenterbackend.model.User;
import com.yupi.usercenterbackend.model.request.UserLoginRequest;
import com.yupi.usercenterbackend.model.request.UserRegisterRequest;
import com.yupi.usercenterbackend.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long register(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest==null){
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount,password,checkPassword)){
            return null;
        }
        return userService.userRegister(userAccount, password, checkPassword);
    }

    @PostMapping("/login")
    public User login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest==null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,password)){
            return null;
        }
        return userService.userLogin(userAccount, password,request);
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request){
        User userObj = (User)request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj == null){
            return null;
        }
        User user = userService.getById(userObj.getId());
        return  userService.getSafe(user);
    }

    @GetMapping("/search")
    public List<User> getUserList(String username, HttpServletRequest request){
        if (!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(username),User::getUsername,username);
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafe(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request){
        if (!isAdmin(request)){
            return false;
        }
        if (id <= 0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        //鉴权，仅管理员可查询
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }
}
