package com.yupi.usercenterbackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.usercenterbackend.common.BaseResponse;
import com.yupi.usercenterbackend.common.ErrorCode;
import com.yupi.usercenterbackend.common.ResultUtils;
import com.yupi.usercenterbackend.exception.BusinessException;
import com.yupi.usercenterbackend.model.domain.User;
import com.yupi.usercenterbackend.model.domain.request.CurrentUserRequest;
import com.yupi.usercenterbackend.model.domain.request.UserLoginRequest;
import com.yupi.usercenterbackend.model.domain.request.UserRegisterRequest;
import com.yupi.usercenterbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.yupi.usercenterbackend.constant.RedisConstant.TOKEN_KEY;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8000"})
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    RedisTemplate redisTemplate;

    /**
     * 用户注册
     * 9
     * +
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 校验
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<String> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        String uuid = userLoginRequest.getUuid();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String key = userService.userLogin(userAccount, userPassword, uuid, request);
        return ResultUtils.success(key);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(@RequestBody CurrentUserRequest userRequest) {
        String key = TOKEN_KEY + userRequest.getUuid();
        Integer result = Math.toIntExact(redisTemplate.opsForHash().delete(key, userRequest.getUserAccount()));
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户
     *
     * @param userRequest
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(CurrentUserRequest userRequest) {
        String key = TOKEN_KEY + userRequest.getUuid();
        System.out.println("key = " + key);
        User user = (User) redisTemplate.opsForHash().get(key, userRequest.getUserAccount());
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return ResultUtils.success(user);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String currentUserAccount, String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUserAccount, currentUserAccount);
        User currentUser = userService.getOne(wrapper);
        if (currentUser.getUserRole() == 0) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchByTags(@RequestParam(required = false) List<String> tagNameList) {
        System.out.println("tagNameList = " + tagNameList);
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        List<User> users = userService.queryUsersByTags(tagNameList);
        return ResultUtils.success(users);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }


}
