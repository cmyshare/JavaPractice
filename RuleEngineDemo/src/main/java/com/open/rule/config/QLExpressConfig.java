package com.open.rule.config;

import com.alibaba.qlexpress4.Express4Runner;
import com.alibaba.qlexpress4.InitOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * QLExpress配置类
 * 
 * @author Qoder
 */
@Configuration
public class QLExpressConfig {

    /**
     * 创建ExpressRunner Bean
     * @return ExpressRunner实例
     */
    @Bean
    public Express4Runner expressRunner() {
        Express4Runner runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
        // 创建基础的ExpressRunner实例
        // 注意：新版本的QLExpress可能不支持setPrecise和setTrace方法
        return runner;
    }
}