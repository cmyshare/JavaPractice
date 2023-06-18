package com.open.easyexcel.example;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/4/28 0028 16:08
 * @description 写操作
 */
public class TestEasyExcelWrite {
    public static void main(String[] args) {

        //实现excel写的操作
        //1 设置写入文件夹地址和excel文件名称
        String filename = "F:\\IDEA2022\\workmenu\\easyExcelDemo\\src\\main\\java\\com\\easyexcel\\data\\test.xlsx";
        // 2 调用easyexcel里面的方法实现写操作
        // write方法两个参数：第一个参数文件路径名称，第二个参数实体类class、sheet为表名、doWrite为写入数据
        EasyExcel.write(filename, DemoData.class).sheet("老师列表").doWrite(getData());

    }

    //创建方法返回写的数据_list集合
    private static List<DemoData> getData() {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            //模型中有表头
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("lucy"+i);
            list.add(data);
        }
        return list;
    }
}
