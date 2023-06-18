package com.open.easyexcel.test;
/**
 * @author cmy
 * @version 1.0
 * @date 2022/5/17 0017 17:06
 * @description 字符串压缩
 */

public class StringIndex {
    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     *
     * @param str string字符串
     * @return string字符串
     */
    public static String compress(String str) {
        // write code here
        StringBuilder parentStr=new StringBuilder(str);

        StringBuilder sonStr=new StringBuilder();

        for(int i=0;i<str.length();i++){
            char c=str.charAt(i);
            if(c=='['){

            }
            System.out.println(c);
        }

        //得到最外层开始下标
        int start= str.indexOf('[');
        //得到最外层开始下标
        int end=str.lastIndexOf(']');
        System.out.println(start);
        System.out.println(end);
        String[] split = str.substring(start, end + 1).split("|");
        System.out.println(split[1]);

        return parentStr.toString();
    }

    public static void main(String[] args) {
        String compress = compress("HG[3|B[2|CA]]F");
        System.out.println(compress);
    }
}
