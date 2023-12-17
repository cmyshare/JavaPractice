package com.cmyshare.open.springscheduled;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2023/11/4 18:25
 * @desc 测试定时任务
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *      1、@EnableScheduling 开启定时任务
 *      2、@Scheduled开启一个定时任务
 *      3、自动配置类TaskSchedulingAutoConfiguration
 *
 * 异步任务
 *      1、@EnableAsync:开启异步任务，无需手动提交线程池
 *      2、@Async：给希望异步执行的方法标注
 *      3、自动配置类TaskExecutionAutoConfiguration 属性TaskSchedulingProperties
 *
 *
 * 注意：Scheduled表达式和cron表达式区别：
 *      1、在Spring中表达式是6位组成，不允许第七位的年份
 *      2、在周几的的位置,1-7代表周一到周
 *      3、定时任务不该阻塞。默认是阻塞的
 * 解决方案：使用异步任务 + 定时任务来完成定时任务不阻塞的功能。
 */

@Component //组件放入容器中
@Slf4j
public class HelloSpringScheduled {

    /**
     * Scheduled表达式和cron表达式区别：
     * 1、在Spring中表达式是6位组成，不允许第七位的年份
     * 2、在周几的的位置,1-7代表周一到周日
     * 3、定时任务不该阻塞。默认是阻塞的
     *      1）、可以让业务以异步的方式，自己提交到线程池
     *              CompletableFuture.runAsync(() -> {
     *         },execute);
     *
     *      2）、支持定时任务线程池；设置属性TaskSchedulingProperties
     *        spring.task.scheduling.pool.size: 5
     *        有些springboot版本中不好使
     *
     *      3）、让定时任务异步执行@Async
     *          异步任务
     *      解决：使用异步任务 + 定时任务来完成定时任务不阻塞的功能
     *
     */
    @Async
    @Scheduled(cron = "*/5 * * ? * 4") //使用Spring的Scheduled注解，在每天的每个小时的第5分钟、10分钟、15分钟等，每周的星期四执行。
    public void hello() {
        System.out.println("Hello**********************");
        //try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

    }

    @Scheduled(cron = "0/10 * * * * *") //每隔10秒执行一次
    public void execute() {
        System.out.println("Scheduled task is running... ...");
    }
}
