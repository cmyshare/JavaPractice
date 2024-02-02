package com.open.javabasetool;

import lombok.Data;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/1/10 21:43
 * @desc
 */
@Data
public class Users implements Diffable<Users> {
    private String name;
    private int age;
    private List<User> userList;


    /**
     * 复合对比diff方法
     * @param obj
     * @return
     */
    public List<Object> returnDiff(Users obj){
        List<Object> objects=new ArrayList<Object>();
        //对比基础类
        DiffResult diff = this.diff(obj);
        objects.addAll(diff.getDiffs());
        //对比列表
        List<Map<String,List<Object>>> userListDiff = this.diffList(this.userList, obj.getUserList());
        objects.addAll(userListDiff);
        return objects;
    }

    /**
     * 基础对象diff方法
     * @param obj 新值
     * @return
     */
    public DiffResult diff(Users obj) {
        // No need for null check, as NullPointerException correct if obj is null
        DiffResult diffResult = new DiffBuilder(this, obj, ToStringStyle.JSON_STYLE)
                .append("名称", this.name, obj.name)
                .append("年龄", this.age, obj.age)
                .build();
        return diffResult;
    }


    /**
     * 复合列表对比
     * @param oldList
     * @param newList
     * @return
     */
    public List<Map<String,List<Object>>> diffList(List<User> oldList,List<User> newList) {
        List<Map<String,List<Object>>> objects=new ArrayList<>();
        for (int i = 0; i < oldList.size(); i++) {
            Map<String,List<Object>> map=new HashMap<>();
            DiffResult diff = oldList.get(i).diff(newList.get(i));
            map.put("列表"+i,diff.getDiffs());
            objects.add(map);
        }
        return objects;
    }

    /**
     * 面对复合类型对象处理，比如列表、多维列表，依次类推。
     */

}
