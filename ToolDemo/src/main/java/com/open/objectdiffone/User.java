package com.open.objectdiffone;

import lombok.Data;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/1/10 21:43
 * @desc
 */
@Data
public class User implements Diffable<User> {
    private String name;
    private int age;

    public DiffResult diff(User obj) {
        // No need for null check, as NullPointerException correct if obj is null
        return new DiffBuilder(this, obj, ToStringStyle.JSON_STYLE)
                .append("名称", this.name, obj.name)
                .append("年龄", this.age, obj.age)
                .build();
    }
}
