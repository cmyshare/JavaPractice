package com.open.objectdiffone;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/1/10 21:43
 * @desc
 */
@Data
public class Student {

    /**
     * 学生id
     */
    @NotNull(message = "学生id不能为空！")
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空！")
    private String name;
    /**
     * 性别
     */
    @NotNull(message = "性别不能为空！")
    private int sex;
    /**
     * 学生书籍ids
     */
    @NotNull(message = "学生书籍ids不能为空！")
    private Long[] ids;
    /**
     * 个人列表
     */
    @NotNull(message = "个人列表不能为空！")
    private List<User> users;



    //public Student() {
    //}
    //
    //public Student(String name, int sex) {
    //    this.name = name;
    //    this.sex = sex;
    //}
    //
    //public String getName() {
    //    return name;
    //}
    //
    //public void setName(String name) {
    //    this.name = name;
    //}
    //
    //public int getSex() {
    //    return sex;
    //}
    //
    //public void setSex(int sex) {
    //    this.sex = sex;
    //}
    //
    //@Override
    //public boolean equals(Object o) {
    //    if (this == o) return true;
    //    if (o == null || getClass() != o.getClass()) return false;
    //    Student student = (Student) o;
    //    return sex == student.sex && Objects.equals(name, student.name);
    //}
    //
    //@Override
    //public int hashCode() {
    //    return Objects.hash(name, sex);
    //}
    //
    //@Override
    //public String toString() {
    //    return "Student{" +
    //            "name='" + name + '\'' +
    //            ", sex=" + sex +
    //            '}';
    //}
    //
    //@Override
    //protected Object clone() throws CloneNotSupportedException {
    //    return super.clone();
    //}
    //
    //@Override
    //protected void finalize() throws Throwable {
    //    super.finalize();
    //}
}
