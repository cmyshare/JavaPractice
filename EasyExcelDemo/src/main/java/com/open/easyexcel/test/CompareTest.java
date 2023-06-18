package com.open.easyexcel.test;

import com.open.easyexcel.mapper.UserMapper;
import com.open.easyexcel.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/5/16 0016 14:31
 * @description 比较用户小区信息
 */
@RestController
public class CompareTest {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/userCompare")
    public String CompareUserAddress(){
        List<User> userList=userMapper.getUserList();
        return "数量"+userList.size();
    }

}
