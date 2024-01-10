//package com.open.activiti.service;
//
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//
///**
// * @version 1.0
// * @Author cmy
// * @Date 2023/12/19 16:21
// * @desc
// */
//@Component
//public class SelfUserDetailsServiceImpl implements UserDetailsService {
//
//    /**
//     * Activiti7默认和Spring Security集成了，当我们的项目中不需要Spring Security时，
//     * 需要排除Spring Security配置，但是Activiti7在代码中强耦合了SpringSecurity，所以我们在使用activiti的Api查询候选人任务时会遇到异常
//     *
//     * taskService.taskCandidateUser(candidateUser);
//     *
//     * 该 Api 会调用 SpringSecurity 中的 UserDetailsService 类型的bean中的 loadUserByUsername 方法.然后是找不到的,所以报错了
//     *
//     * @param username
//     * @return
//     * @throws UsernameNotFoundException
//     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return new User(username,"", Collections.emptyList());
//    }
//}
//
