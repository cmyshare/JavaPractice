package com.open.easyexcel.dynamictable;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/3/16 23:37
 * @description EasyExcel动态表格导出
 */

@RestController
public class DynamicTableExportController {

    @PostMapping("/export")
    public void export(HttpServletResponse response) throws IOException {

        //模拟数据
        //一般动态数据使用的是List，然后内部使用Map进行数据的接受
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            //组装一行数据，一行中每个字段的key为String，值为Object
            HashMap<String, Object> map = new HashMap<>();
            for (int j = 0; j < 5; j++) {
                map.put("title" + j, i + "," + j);
            }
            //这个用于测试值如果为null时，能否进行默认值填充
            map.put("title5", null);
            list.add(map);
        }


        //使用LinkedHashMap进行表头字段映射
        LinkedHashMap<String, DynamicExcelData> nameMap = new LinkedHashMap<>();
        //String表示这个表头字段映射key和一行数据中字段的key为String是一样的
        nameMap.put("title3", new DynamicExcelData("爱好", "0"));
        nameMap.put("title4", new DynamicExcelData("小名", "0"));
        nameMap.put("title5", new DynamicExcelData("空白字段", "0"));
        nameMap.put("title1", new DynamicExcelData("年龄", "0"));
        nameMap.put("title0", new DynamicExcelData("姓名", "0"));
        nameMap.put("title2", new DynamicExcelData("职业", "0"));

        //调用方法,方法已在步骤3进行介绍
        dynamicExport(response, nameMap, list, "EasyExcel动态表格导出方案");

    }


    /**
     * EasyExcel动态表格导出公共方法
     *
     * @param response  响应
     * @param nameMap   表格头数据
     * @param list      表格内容数据
     * @param sheetName 表格名
     * @throws IOException
     */
    public static void dynamicExport(HttpServletResponse response,
                                     LinkedHashMap<String, DynamicExcelData> nameMap,
                                     List<Map<String, Object>> list,
                                     String sheetName) throws IOException {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        if (ObjectUtil.isEmpty(nameMap)) {
            throw new RuntimeException("表头数据为空，请检查!");
        }

        //初始化表格内容导出模板
        int size = list.size();
        List<List<String>> dataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            //加入一行
            dataList.add(new ArrayList<>());
        }

        //获取表格头数据，加入表格头导出模板
        ArrayList<List<String>> head = new ArrayList<>();
        ////排序表格头数据
        //nameMap.entrySet()
        //        .stream()
        //        .sorted(Map.Entry.<String, DynamicExcelData>comparingByKey())
        //        .forEach(System.out::println);
        //排序表格头数据,收集为List<Map.Entry<String, DynamicExcelData>>
        List<Map.Entry<String, DynamicExcelData>> collect = nameMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, DynamicExcelData>comparingByKey()).collect(Collectors.toList());
        //循环表格头数据
            for (Map.Entry<String, DynamicExcelData> titleMap : collect) {
            //取出表格头一行中一个字段值
            DynamicExcelData data = titleMap.getValue();
            //加入表格头导出模板
            head.add(Collections.singletonList(data.getName()));
        }

        //获取表格内容，数据重组
        for (int i = 0; i < list.size(); i++) {
            //取出表格内容一行
            Map<String, Object> map = list.get(i);
            //表格内容导出模板一行
            List<String> columns = dataList.get(i);
            //循环表格头
            for (Map.Entry<String, DynamicExcelData> sortNameEntry : nameMap.entrySet()) {
                //表格头每个字段key
                String key = sortNameEntry.getKey();
                //从表格内容一行中map，取出表头这个key的值
                Object value = map.get(key);
                //表格内容导出模板一行，依次加入字段值
                columns.add(value != null ? String.valueOf(value) : sortNameEntry.getValue().getDefaultValue());
            }

        }

        //设置导出格式
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        //调用EasyExcel的write方法，实现导出功能
        EasyExcel.write(response.getOutputStream()).head(head)
                .sheet(sheetName).doWrite(dataList);
    }

}
