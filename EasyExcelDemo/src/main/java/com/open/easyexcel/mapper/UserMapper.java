package com.open.easyexcel.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.open.easyexcel.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> getUserList();
}
