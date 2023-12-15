package com.yupi.usercenterbackend;

import com.yupi.usercenterbackend.mapper.UserMapper;
import com.yupi.usercenterbackend.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class UserCenterBackendApplicationTests {

    @Resource
    UserMapper userMapper;
    @Test
    void contextLoads() {
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5,userList.size());
        userList.forEach(System.out::println);
    }

}
