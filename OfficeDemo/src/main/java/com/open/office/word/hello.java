package com.open.office.word;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/6/1 17:02
 * @desc
 */
public class hello {
    public static void main(String[] args) {
        String a="2024/06/01/bd7bf3a3384449958eb53d1b3bab2737.docx";
        String b="委托加工协议240306CMT（修订版）.docx";

        System.out.println(a);
        System.out.println(b);
        // 注意：因为"."是正则表达式中的特殊字符，所以需要用"\\."来表示
        System.out.println(a.split("\\.")[0]+".pdf");

        
    }
}
