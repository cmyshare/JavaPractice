package com.open;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2025/10/11 14:02
 * @desc
 */
public class hello {
    public static void main(String[] args) {
        Integer a = 1000;
        Integer b = 1000;
        System.out.println(a == b); // false
        System.out.println(a.equals(b));

        Integer c = 100;
        Integer d = 100;
        System.out.println(c == d); // true
    }
}
