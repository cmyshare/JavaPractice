package com.open.activiti.configration;


import com.open.activiti.entity.Permission;
import com.open.activiti.reposity.PermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@Slf4j
public class SecurityConfig {

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 定义一个能够与HttpServlet请求匹配的过滤器链。以便决定它是否适用于该请求。用于配置FilterChainProxy。
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //将基于SpEL表达式的基于URL的授权添加到应用程序中。
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
                authorizeRequests = http.csrf().disable().authorizeRequests();
        //方式二：配置来源于数据库
        // 1.查询到所有的权限
        List<Permission> allPermission = permissionMapper.findAllPermission();
         //2.分别添加权限规则
        allPermission.forEach((p -> {
            authorizeRequests.antMatchers(p.getUrl()).hasAnyAuthority(p.getName()) ;
        }));

        //授权请求
        authorizeRequests.antMatchers("/**").fullyAuthenticated()
                .anyRequest().authenticated().and().formLogin();
        return http.build();
    }

    /**
     * 用于自定义WebSecurity的回调接口。WebSecurityConfiguration将自动使用此类型的Bean来自定义WebSecurity
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring().antMatchers("/css/**");
            web.ignoring().antMatchers("/js/**");
            web.ignoring().antMatchers("/img/**");
            web.ignoring().antMatchers("/plugins/**");
            web.ignoring().antMatchers("/login.html");
        };
    }


    /**
     * 用于编码密码的服务接口。首选的实现方式是BCryptPasswordEncoder。
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        /**
         * 使用BCrypt强哈希函数的PasswordEncoder的实现。
         * 客户端可以选择提供一个“版本”（2a、2b、2y）和一个“强度”（也称为BCrypt中的日志轮次）以及一个SecureRandom实例。
         * 强度参数越大，对密码进行散列所需的工作量（指数级）就越多。默认值为10。
         */
        return new BCryptPasswordEncoder();
    }

}
