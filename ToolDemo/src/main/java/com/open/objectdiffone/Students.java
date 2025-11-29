package com.open.objectdiffone;

import com.open.javabasetool.objectdiffone.Student;
import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/1/10 21:43
 * @desc
 */
@Data
public class Students {
    private String name;
    private int sex;
    private List<Student> students;
}
