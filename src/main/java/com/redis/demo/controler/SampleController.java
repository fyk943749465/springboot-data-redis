package com.redis.demo.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * redis生产者测试
     * @param data
     * @return
     */
    @GetMapping("/send1")
    String send1(String data) {
        redisTemplate.convertAndSend("redis-test-topic", data);
        return "success";
    }
}

