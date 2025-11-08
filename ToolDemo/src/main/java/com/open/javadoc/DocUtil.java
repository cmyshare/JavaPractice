package com.open.javadoc;//package com.open.javabasetool.javadoc;
//
//import com.sun.javadoc.ClassDoc;
//import com.sun.javadoc.FieldDoc;
//import com.sun.javadoc.RootDoc;
//import com.sun.tools.javadoc.Main;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @version 1.0
// * @Author cmy
// * @Date 2024/1/19 14:17
// * @desc
// */
//public class DocUtil {
//    /**
//     * 会自动注入
//     */
//    private static RootDoc rootDoc;
//
//    /**
//     * 会自动调用这个方法
//     *
//     * @param root root
//     * @return true
//     */
//    public static boolean start(RootDoc root) {
//        rootDoc = root;
//        return true;
//    }
//
//    /**
//     * 生成文档
//     *
//     * @param beanFilePath 注意这里是.java文件绝对路径
//     * @return 文档注释
//     */
//    public static DocVO execute(String beanFilePath) {
//        Main.execute(new String[]{"-doclet", DocUtil.class.getName(), "-docletpath",
//                DocUtil.class.getResource("/").getPath(), "-encoding", "utf-8", beanFilePath});
//
//        ClassDoc[] classes = rootDoc.classes();
//        Arrays.stream(classes).forEach(System.out::println);
//
//        if (classes == null || classes.length == 0) {
//            return null;
//        }
//        ClassDoc classDoc = classes[0];
//        // 获取属性信息数组(属性信息：字段名、字段类型、字段注释)
//        FieldDoc[] fields = classDoc.fields(false);
//
//        List<DocVO.FieldVO> fieldVOList = new ArrayList<>(fields.length);
//
//        for (FieldDoc field : fields) {
//            System.out.println("fieldName: " + field.name());
//            System.out.println("fieldType: " + field.type().typeName());
//            System.out.println("fieldDesc: " + field.commentText());
//            fieldVOList.add(new DocVO.FieldVO(field.name(), field.type().typeName(), field.commentText()));
//        }
//        return new DocVO(fieldVOList);
//    }
//}
