package com.yupi.usercenterbackend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.usercenterbackend.model.domain.User;
import com.yupi.usercenterbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yupi.usercenterbackend.constant.RedisConstant.USER_SEARCH_KEY;

/**
 * 缓存预热
 */
@Component
@Slf4j
public class PreCashJob {

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    UserService userService;

    List<Long> mainUserList = Collections.singletonList(3L);

    // 每天晚上 22:56 执行一次任务
    @Scheduled(cron = "0 14 23 * * ?")
    public void doCashRecommendTask() {
        for (Long userId : mainUserList) {
            //没有直接查询数据库
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
            // 执行你的任务逻辑
            String key = USER_SEARCH_KEY + userId;

            //写缓存
            try {
                redisTemplate.opsForValue().set(key,userPage,60, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error("redis set key error");
            }
        }

    }
}
