package com.open.redis.test;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/11/9 15:59
 * @description 分布式锁&分布式功能框架Redisson
 */

@Controller
public class RedissonTest {
    @Autowired
    private RedissonClient redisson;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 8.1. 可重入锁（Reentrant Lock）
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/hello")
    public String hello() {

        //1、获取一把锁，只要锁的名字一样，就是同一把锁
        RLock myLock = redisson.getLock("my-lock");

        //2、加锁
        myLock.lock(); //阻塞式等待，直到获得锁为止。默认加的锁都是30s

        //redisson 可重入锁Reentrant Lock有点：
        //1）、锁的自动续期，如果业务超长，运行期间自动锁上新的30s。不用担心业务时间长，锁自动过期被删掉
        //2）、加锁的业务只要运行完成，就不会给当前锁续期，即使不手动解锁，锁默认会在30s内自动过期，不会产生死锁问题

        //最佳实践：10秒钟自动解锁,自动解锁时间一定要大于业务执行时间
        // myLock.lock(10,TimeUnit.SECONDS);
        //问题：在锁时间到了以后，不会自动续期
        //1、如果我们传递了锁的超时时间，就发送给redis执行脚本，进行占锁，默认超时就是 我们制定的时间
        //2、如果我们指定锁的超时时间，就使用 lockWatchdogTimeout = 30 * 1000 【看门狗默认时间】

        //只要占锁成功，就会启动一个定时任务【重新给锁设置过期时间，新的过期时间就是看门狗的默认时间】,每隔10秒都会自动的再次续期，续成30秒
        // internalLockLeaseTime主流(看门狗时间)
        try {
            System.out.println("加锁成功，执行业务..." + Thread.currentThread().getId());
            try { TimeUnit.SECONDS.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //3、解锁  假设解锁代码没有运行，Redisson会不会出现死锁
            System.out.println("释放锁..." + Thread.currentThread().getId());
            myLock.unlock();
        }

        return "hello";
    }


    /**
     * 8.5. 读写锁（ReadWriteLock）
     * 保证一定能读到最新数据，修改期间，写锁是一个排它锁（互斥锁、独享锁），读锁是一个共享锁
     * 写锁没释放读锁必须等待
     * 读 + 读 ：相当于无锁，并发读，只会在Redis中记录好，所有当前的读锁。他们都会同时加锁成功
     * 写 + 读 ：必须等待写锁释放
     * 写 + 写 ：阻塞方式
     * 读 + 写 ：有读锁。写也需要等待
     * 只要有读或者写的存都必须等待
     * @return
     */
    @GetMapping(value = "/write")
    @ResponseBody
    public String writeValue() {
        String s = "";
        //获取读写锁接口
        RReadWriteLock readWriteLock = redisson.getReadWriteLock("rw-lock");
        //获取写锁
        RLock rLock = readWriteLock.writeLock();
        try {
            //1、改数据加写锁，读数据加读锁
            rLock.lock();
            s = UUID.randomUUID().toString();
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            ops.set("writeValue",s);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //解锁
            rLock.unlock();
        }

        return s;
    }
    /**
     * 8.5. 读写锁（ReadWriteLock）
     * @return
     */
    @GetMapping(value = "/read")
    @ResponseBody
    public String readValue() {
        String s = "";
        //获取读写锁接口
        RReadWriteLock readWriteLock = redisson.getReadWriteLock("rw-lock");
        //获取读锁接口
        RLock rLock = readWriteLock.readLock();
        try {
            //加锁
            rLock.lock();
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            s = ops.get("writeValue");
            try { TimeUnit.SECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //解锁
            rLock.unlock();
        }

        return s;
    }


    /**
     * 8.6. 信号量（Semaphore）
     * 车库停车
     * 3车位
     * 应用场景：信号量也可以做分布式限流
     */
    @GetMapping(value = "/park")
    @ResponseBody
    public String park() throws InterruptedException {
        //信号量对象
        RSemaphore park = redisson.getSemaphore("park");

        //阻塞方法，获取一个信号、获取一个值,占一个车位
        park.acquire();

        //tryAcquire快速失败抛出
        boolean flag = park.tryAcquire();
        if (flag) {
            //执行业务
        } else {
            return "error";
        }

        return "ok=>" + flag;
    }
    @GetMapping(value = "/go")
    @ResponseBody
    public String go() {
        //信号量对象
        RSemaphore park = redisson.getSemaphore("park");
        //释放一个车位
        park.release();
        return "ok";
    }


    /**
     * 8.8. 闭锁（CountDownLatch）
     * 放假、锁门
     * 1班没人了
     * 5个班，全部走完，我们才可以锁大门
     * 应用场景：分布式闭锁，JUC也有。在多线程任务调度中使用，比如多个线程任务全部完成才算全部完成。
     */
    @GetMapping(value = "/lockDoor")
    @ResponseBody
    public String lockDoor() throws InterruptedException {
        //闭锁对象
        RCountDownLatch door = redisson.getCountDownLatch("door");
        //等待个数
        door.trySetCount(5);
        //等待闭锁完成
        door.await();
        return "放假了...";
    }
    @GetMapping(value = "/gogogo/{id}")
    @ResponseBody
    public String gogogo(@PathVariable("id") Long id) {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.countDown();       //计数-1
        return id + "班的人都走了...";
    }
}
