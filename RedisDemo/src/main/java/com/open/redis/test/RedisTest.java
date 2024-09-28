package com.open.redis.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author cmy
 * @version 1.0
 * @date 2023/4/23 22:54
 * @description Redis缓存测试类
 * <p>
 * Redis 常用的数据结构有哪些？
 * 5 种基础数据结构 ：String（字符串）、List（列表）、Set（集合）、Hash（散列）、Zset（有序集合）。
 * <p>
 * 3 种特殊数据结构 ：HyperLogLogs（基数统计）、Bitmap （位存储）、Geospatial (地理位置)。
 * BitMap（2.2 版新增）：二值状态统计的场景，比如签到、判断用户登陆状态、连续签到用户总数等；
 * HyperLogLog（2.8 版新增）：海量数据基数统计的场景，比如百万级网页 UV 计数等；
 * GEO（3.2 版新增）：存储地理位置信息的场景，比如滴滴叫车；
 * Stream（5.0 版新增）：消息队列，相比于基于 List 类型实现的消息队列，有这两个特有的特性：自动生成全局唯一消息ID，支持以消费组形式消费数据。
 */

@RestController
public class RedisTest {
    /**
     * Spring Boot Data(数据) Redis 中提供了 RedisTemplate 和 StringRedisTemplate；
     * StringRedisTemplate 是 RedisTemplate 的子类，两个方法基本一致，不同之处在于 操作的数据类型不同：
     * <p>
     * RedisTemplate 两个泛型都是 Object，意味着存储的 key 和 value 都可以是一个对象
     * StringRedisTemplate 两个泛型都是 String，意味着存储的 的 key 和 value 都只能是字符串。
     * 注：使用 RedisTemplate 默认是将对象序列化到 Redis 中，所以 放入的对象必须实现对象序列化接口。
     * <p>
     * 注：两者的 数据是不共通的；也就是说 StringRedisTemplate 只能管理 StringRedisTemplate 里面的数据，
     * RedisTemplate 只能管理 RedisTemplate 中的数据。
     * <p>
     * https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/RedisTemplate.html
     */
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 添加string方法，写入字符串“Hello World!”，然后读取该字符串并返回该字符串
     * https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/ValueOperations.html
     * <p>
     * String 类型的应用场景：缓存对象、常规计数、分布式锁setnx、共享 session 信息等
     *
     * @return
     */
    @GetMapping("/string")
    public String stringTest() {
        //TODO 基础操作
        //set(K key, V value, long timeout, TimeUnit unit)
        //设置value和过期timeout时间key
        redisTemplate.opsForValue().set("str", "Hello World!", 60, TimeUnit.SECONDS);
        //获取键为key对应的value
        String str = (String) redisTemplate.opsForValue().get("str");

        //TODO 缓存对象 直接缓存整个对象的JSON

        //TODO 常规计数 redis由于incrby命令可以实现原子性的递增

        //TODO 分布式锁 SET 命令有个 NX 参数可以实现「key不存在才插入」

        //TODO 共享Session信息 Redis对这些Session信息进行统一的存储和管理

        return str;
    }

    /**
     * key模糊搜索的到keys
     */
    @GetMapping("/keyLikeQuery")
    public Set<String> keyLikeQuery(@RequestParam("searchStr") String searchStr) {
        //keys方法可能会在 Redis 中进行全量的键扫描，这在大型数据集的情况下可能会非常耗时。如果可能的话，可以考虑使用其他更高效的方法来实现类似的功能，例如使用 Redis 的SCAN命令。
        Set<String> keys = redisTemplate.keys("*" + searchStr + "*");
        return keys;
    }

    /**
     * key模糊搜索的到keys-SCAN命令
     */
    @GetMapping("/keyLikeQueryTwo")
    public Set<String> keyLikeQueryTwo(@RequestParam("searchStr") String searchStr) {
        //Set<String> resultKeys = new HashSet<>();
        //String cursor = "0";
        //do {
        //    ScanParams params = new ScanParams();
        //    params.match("*" + searchStr + "*");
        //    ScanResult<String> scanResult = (ScanResult<String>) redisTemplate.execute((RedisCallback<ScanResult<String>>) connection -> {
        //        ScanResult<String> result = connection.scan(cursor, params);
        //        cursor = result.getStringCursor();
        //        return result;
        //    });
        //    resultKeys.addAll(scanResult.getResult());
        //} while (!cursor.equals("0"));
        //return resultKeys;
        return null;
    }

    /**
     * 添加list方法，双向链表，左右添加。
     * https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/ListOperations.html
     * <p>
     * List 类型的应用场景：消息队列（但是有两个问题：1. 生产者需要自行实现全局唯一 ID；2. 不能以消费组形式消费数据）等。
     *
     * @return
     */
    @GetMapping("/list")
    public List<String> listTest() {
        //TODO 基础操作
        //Redis 列表具体操作。
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        //从左边添加
        listOperations.leftPush("list", "left");
        listOperations.leftPush("list", "left1");
        //从右边添加
        listOperations.rightPush("list", "right");
        listOperations.rightPush("list", "right1");

        //获取key对应list区间[i,j]的元素，注：从左边0开始，包头包尾
        List<String> list = listOperations.range("list", 0, 3);

        //TODO 消息队列 保证消息保序、处理重复的消息和保证消息可靠性。
        //TODO Redis2.0引入了发布订阅(pub/sub)解决了List实现消息队列没有广播机制的问题。
        //TODO Redis 5.0 新增加的一个数据结构 Stream 来做消息队列。支持发布/订阅模式、按照消费者组进行消费、消息持久化RDB和AOF。
        //TODO 使用 Redis 来实现消息队列还是有很多欠缺的地方比如消息丢失和堆积问题不好解决。不推荐redis作为消息队列

        //TODO 微博TimeLine: 有人发布微博，用lpush加入时间轴，展示新的列表信息。

        return list;
    }

    /**
     * 添加set方法，去重集合
     * https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/SetOperations.html
     * <p>
     * Set类型应用场景：聚合计算（并集、交集、差集）场景，比如点赞、共同关注、抽奖活动等。
     *
     * @return
     */
    @GetMapping("/set")
    public Set<String> setTest() {
        //TODO 基础操作
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        //add(K key, V... values) 添加元素
        setOperations.add("set", "Hello");
        setOperations.add("set", "Hello");
        setOperations.add("set", "World!");
        setOperations.add("set", "World!");
        //获取set中的所有元素key
        Set<String> set = setOperations.members("set");

        //TODO 点赞 key 是文章id，value 是用户id

        //TODO 共同关注 Set类型支持交集运算，所以可以用来计算共同关注的好友、公众号等。key 可以是用户id，value 则是已关注的公众号的id

        //TODO 抽奖活动 存储某活动中中奖的用户名 ，Set 类型因为有去重功能，可以保证同一个用户不会中奖两次。key为抽奖活动名，value为员工名称

        return set;
    }

    /**
     * 添加zset方法，有序集合，根据socre默认升序排序
     * https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/ZSetOperations.html
     * <p>
     * Zset类型应用场景：排序场景，比如排行榜、电话和姓名排序等。
     *
     * @return
     */
    @GetMapping("/zset")
    public Set<String> zsetTest() {
        //TODO 基础操作
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        //添加value到 的排序集key，或者如果它已经存在则更新它score。
        zSetOperations.add("zset", "Hello", 1);
        zSetOperations.add("zset", "World", 2);
        zSetOperations.add("zset", "Java", 3);

        //range(K key, long start, long end) 获取元素之间 start ，并 end 从有序集合。
        Set<String> zset = zSetOperations.range("zset", 0, 2);

        //TODO 排行榜 学生成绩的排名榜、游戏积分排行榜、视频播放排名、电商系统中商品的销量排名

        //TODO 电话、姓名排序 ZRANGEBYLEX或ZREVRANGEBYLEX可以帮助我们实现电话号码或姓名的排序

        return zset;
    }

    /**
     * 添加hash方法
     * https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/HashOperations.html
     * <p>
     * Hash类型应用场景：缓存对象、购物车等。
     *
     * @return
     */
    @GetMapping("/hash")
    public String hashTest() {
        //TODO 基础操作
        //Redis 映射在哈希上工作的特定操作
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        //put(H key, HK hashKey, HV value) 设置value哈希的hashKey。
        hashOperations.put("key", "hashKey", "Hello");
        //hashKey从处的散列中获取given的值key
        String s = hashOperations.get("key", "hashKey");
        System.out.println("hashKey从处的散列中获取given的值key" + s);

        //TODO 缓存对象 Hash类型的（key，field， value）的结构与对象的（对象id， 属性， 值）的结构相似

        //TODO 购物车 以用户id为 key，商品id为field，商品数量为value，恰好构成了购物车的3个要素
        return s;
    }
}
