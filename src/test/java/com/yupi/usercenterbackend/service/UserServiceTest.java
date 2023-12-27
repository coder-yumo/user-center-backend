package com.yupi.usercenterbackend.service;

import com.yupi.usercenterbackend.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *
 * @author linli
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        for (int i = 0; i < 10000000; i++) {
            User user = new User();
            user.setUsername("abin" + 1);
            user.setUserAccount("abin" + i);
            user.setAvatarUrl("https://thirdwx.qlogo.cn/mmopen/vi_32/AUpl4UT9k4TD5GqoCgHG8dEnlHuMbGhB10Uic06euXjbWhlu9kb6PwzsTq1e1aewmFnBKcDGqX1HUltp3YHFPUA/132");
            user.setGender(0);
            user.setUserPassword("123456789");
            user.setPhone("1" + i);
            user.setEmail("1" + i);
            user.setPlanetCode("6" + i);
            user.setTags("[\"大一\",\"java\",\"c++\",\"python\",\"c\"]");
            user.setProfile("大家好，我是渣渣辉，是兄弟就来砍我");
            userService.save(user);
        }
    }
}