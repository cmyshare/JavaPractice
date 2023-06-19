package com.open.mysql.mapper;

import com.open.mysql.model.Person;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonMapper {
    public void insert(Person person);
}
