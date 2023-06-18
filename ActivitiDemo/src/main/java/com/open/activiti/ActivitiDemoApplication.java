package com.open.activiti;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author cmy
 * @version 1.0
 * @date 2023/4/4 0:16
 * @description ActivitiDemo启动类
 *
 * Alfresco软件在2010年5月17日宣布Activiti业务流程管理（BPM）开源项目的正式启动，
 * 其首席架构师由业务流程管理BPM的专家 Tom Baeyens担任，Tom Baeyens就是原来jbpm的架构师，
 * 而jbpm是一个非常有名的工作流引擎，当然activiti也是一个工作流引擎。
 *
 * Activiti是一个工作流引擎， activiti可以将业务系统中复杂的业务流程抽取出来，
 * 使用专门的建模语言BPMN2.0进行定义，业务流程按照预先定义的流程进行执行，
 * 实现了系统的流程由activiti进行管理，减少业务系统由于流程变更进行系统升级改造的工作量，
 * 从而提高系统的健壮性，同时也减少了系统开发维护成本。
 *
 * 官方网站：https://www.activiti.org/
 * 案例参考链接：https://zhuanlan.zhihu.com/p/544066547
 * 核心API：https://blog.csdn.net/qq_31237581/article/details/127619965
 *
 */
@SpringBootApplication
@MapperScan("com.open.activiti.reposity")
public class ActivitiDemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(ActivitiDemoApplication.class, args);

    }


}
