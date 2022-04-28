package com.redis.demo.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
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

    /**
     * Set 类型相关操作
     */
    @GetMapping("/set")
    public String setOps() {

        // 写法1
        redisTemplate.boundSetOps("setKey").add("setValue1", "setValue2", "setValue3");
        // 写法2
        BoundSetOperations<String, String> setKey = redisTemplate.boundSetOps("setKey");
        setKey.add("setValue1", "setValue2", "setValue3");
        // 写法3
        SetOperations<String, String> opsForSet = redisTemplate.opsForSet();
        opsForSet.add("setKey", "setValue1", "setValue2", "setValue3");

        // 获取值
        Set<String> setValues = redisTemplate.boundSetOps("setKey").members();

        // 根据value从一个set中查询，是否存在
        Boolean isEmpty = redisTemplate.boundSetOps("setKey").isMember("setValue");

        // 获取set缓存的长度
        Long size = redisTemplate.boundSetOps("setKey").size();

        // 移除指定的元素
        Long remove = redisTemplate.boundSetOps("setKey").remove("setValue1");

        // 删除指定的key
        Boolean result = redisTemplate.delete("setKey");
        return "success";
    }

    /**
     * list相关的操作
     */
    @GetMapping("/listOps")
    public String listOps() {

        redisTemplate.boundListOps("listKey").leftPush("listLeftValue1");
        redisTemplate.boundListOps("listKey").rightPush("listRightValue2");
        // 将List放入缓存
        ArrayList<String> list = new ArrayList<>();
        list.add("hello world");
        // list 与 可变长参数的转换
        redisTemplate.boundListOps("listKey")
                .rightPushAll(list.stream().toArray(String[]::new));

        BoundListOperations<String, String> listKey = redisTemplate.boundListOps("listKey");
        List<String> strings = listKey.range(0, listKey.size());

        // 从左侧弹出一个元素
        String key1 = redisTemplate.boundListOps("listKey").leftPop();
        // 从右侧弹出一个元素
        String key2 = redisTemplate.boundListOps("listKey").rightPop();
        // 根据索引查询元素
        String element = redisTemplate.boundListOps("listKey").index(1);
        // 根据索引修改值
        redisTemplate.boundListOps("listKey").set(3L, "ListNewValue");
        // 移除元素 移除3个值为value的元素
        redisTemplate.boundListOps("listKey").remove(3L, "value");
        return "success";
    }

    /**
     * zset相关的操作
     *
     * @return
     */
    @GetMapping("/zsetOps")
    public String zsetOps() {

        // 向集合中插入元素并设置值
        redisTemplate.boundZSetOps("zSetKey").add("zSetValue", 100D);

        DefaultTypedTuple<String> p1 = new DefaultTypedTuple<>("zSetValue1", 2.1D);
        DefaultTypedTuple<String> p2 = new DefaultTypedTuple<>("zSetValue2", 3.3D);
        redisTemplate.boundZSetOps("zSetKey").add(new HashSet<>(Arrays.asList(p1, p2)));

        // 按照排名先后（从大到小）打印指定区间内的元素，-1为全部打印
        Set<String> setKey = redisTemplate.boundZSetOps("zSetKey").range(0, -1);
        // 获取指定元素的分数
        Double score = redisTemplate.boundZSetOps("zSetKey").score("zSetValue");
        // 返回集合内的成员个数
        Long size = redisTemplate.boundZSetOps("zSetKey").size();
        // 返回集合内制定分数范围的成员个数
        Long count = redisTemplate.boundZSetOps("zSetKey").count(0D, 4D);
        // 返回指定成员的排名
        Long startRank = redisTemplate.boundZSetOps("zSetKey").rank("zSetValue");
        Long endRank = redisTemplate.boundZSetOps("zSetKey").reverseRank("zSetValue");
        // 从集合中删除指定元素
        redisTemplate.boundZSetOps("zSetKey").remove("zSetValue");
        // 删除指定索引范围内的元素
        redisTemplate.boundZSetOps("zSetKey").removeRange(0L, 3L);
        // 删除指定分数范围内的元素
        redisTemplate.boundZSetOps("zSetKey").removeRangeByScore(0D, 2D);
        // 为指定元素增加分数
        redisTemplate.boundZSetOps("zSetKey").incrementScore("zSetValue1", 3.4D);
        return "success";
    }

}

