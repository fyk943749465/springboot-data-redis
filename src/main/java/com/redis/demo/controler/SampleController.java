package com.redis.demo.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
public class SampleController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * redis 生产者测试，将数据发送到主题 redis-test-topic
     * @param data
     * @return
     */
    @GetMapping("/send1")
    public String send1(String data) {
        redisTemplate.convertAndSend("redis-test-topic", data);
        return "success";
    }

    /**
     * 给key 设置值
     * @return
     */
    @GetMapping("/send2")
    public String send2() {
        // 写法1
        redisTemplate.boundValueOps("StringKey").set("StringValue");
        redisTemplate.boundValueOps("StringKey1").set("StringValue", 1, TimeUnit.MINUTES);
        // 写法2
        BoundValueOperations<String, String> stringKey = redisTemplate.boundValueOps("StringKey");
        stringKey.set("StringValue");
        BoundValueOperations<String, String> stringKey1 = redisTemplate.boundValueOps("StringKey1");
        stringKey1.set("StringValue", 1, TimeUnit.MINUTES);
        // 写法3
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        stringValueOperations.set("StringKey", "StringValue");
        stringValueOperations.set("StringKey1", "StringValue", 1, TimeUnit.MINUTES);
        return "success";
    }

    /**
     * 获取值
     * @return
     */
    @GetMapping("/send3")
    public String send3() {
        // 写法1
        String str = redisTemplate.boundValueOps("StringKey").get();

        // 写法2
        BoundValueOperations<String, String> stringKey = redisTemplate.boundValueOps("StringKey");
        String str2 = stringKey.get();

        // 写法3
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String str3 = valueOperations.get("StringKey");
        return "success";
    }

    /**
     * 删除key
     * @return
     */
    @GetMapping("/delete")
    public String delete() {
        redisTemplate.delete("StringKey");
        return "success";
    }

    /**
     * 递增
     * @return
     */
    @GetMapping("/incr")
    public String incr() {
        redisTemplate.boundValueOps("NumberKey").increment();
        redisTemplate.boundValueOps("NumberKey").increment(10L);
        return "success";
    }

    /**
     * hash 相关操作
     */
    @GetMapping("/hash")
    public String hashOps() {
        // 写法1
        redisTemplate.boundHashOps("HashKey").put("SmallKey", "HashValue");
        // 写法2
        BoundHashOperations<String, Object, Object> hashKey = redisTemplate.boundHashOps("HashKey");
        hashKey.put("SmallKey", "HashValue");
        // 写法3
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put("HashKey", "SmallKey", "HashValue");

        // 写法4
        HashMap<String, String> hashMap = new HashMap<>();
        redisTemplate.boundHashOps("HashKey").putAll(hashMap);


        // 获取所有的小key
        // 写法1
        Set keys1 = redisTemplate.boundHashOps("HashKey").keys();
        // 写法2
        BoundHashOperations hashKey1 = redisTemplate.boundHashOps("HashKey");
        Set keys2 = hashKey1.keys();
        // 写法3
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        Set keys3 = opsForHash.keys("HashKey");

        // 获取所有的values
        // 写法1
        List<Object> values1 = redisTemplate.boundHashOps("HashKey").values();
        // 写法2
        List values2 = hashKey1.values();
        // 写法3
        List<Object> values3 = opsForHash.values("HashKey");

        // 获取所有的 键值队集合
        // 写法1
        Map<Object, Object> entries1 = redisTemplate.boundHashOps("HashKey").entries();
        // 写法2
        hashKey1.entries();
        // 写法3
        opsForHash.entries("HashKey");
        return "success";
    }

    /**
     * hash相关删除操作
     */
    @GetMapping("/deletehash")
    public String deleteHash() {
        // 删除小key
        redisTemplate.boundHashOps("HashKey").delete("SmallKey");
        // 删除大key
        redisTemplate.delete("HashKey");
        return "success";
    }

    /**
     * is exists hash
     */
    @GetMapping("/isexistshash")
    public String isExistsHash() {
        Boolean aBoolean = redisTemplate.boundHashOps("HashKey").hasKey("SmallKey");
        return "success";
    }
}

