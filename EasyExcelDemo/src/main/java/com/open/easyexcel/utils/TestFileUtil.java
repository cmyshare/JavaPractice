package com.open.easyexcel.utils;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/4/29 0029 15:56
 * @description 测试文件输入输出工具类
 */
import java.io.File;
import java.io.InputStream;

public class TestFileUtil {


    public static InputStream getResourcesFileInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("" + fileName);
    }

    public static String getPath() {
        //自定义数据excel路径
        return "D:\\SoftStart\\idea2021\\ideaSave\\JavaPractice\\EasyExcelDemo\\src\\main\\java\\com\\open\\easyexcel\\data";
        //获取当前编译target路径
        //return TestFileUtil.class.getResource("/").getPath();
    }

    public static File createNewFile(String pathName) {
        File file = new File(getPath() + pathName);
        if (file.exists()) {
            file.delete();
        } else {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }
        return file;
    }

    public static File readFile(String pathName) {
        return new File(getPath() + pathName);
    }

    public static File readUserHomeFile(String pathName) {
        return new File(System.getProperty("user.home") + File.separator + pathName);
    }
}

