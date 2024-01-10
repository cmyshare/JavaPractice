package com.open.activiti.service;

import com.open.activiti.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService<T extends User> extends UserDetailsService {


}