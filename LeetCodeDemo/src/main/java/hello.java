/**
 * @author cmy
 * @version 1.0
 * @date 2022/10/12 18:12
 * @description
 */
public class hello {
    public static void main(String[] args) {
        //=======向上转型 Char->int -> long -> float -> double=======================
        float a = (float) (3.4 + 1);
        double b = 3.4 + 1;
        System.out.println(a);
        System.out.println(b);
        System.out.println(3.4 + 1);

        //=============================String 比较===================================
        String s1 = "123";
        String s2 = "123";
        String s3 = new String("123");
        String s4 = "123a";
        String s5 = new String("123a");
        //地址比较
        System.out.println(s1 == s2); //字符串是数字，可用==
        System.out.println(s1 == s3); //s1、s3为不同对象false
        System.out.println(s4 == s5); //字符串是数字英文，不用==false
        //值比较
        System.out.println(s1.equals(s2));
        System.out.println(s1.equals(s3));
        System.out.println(s4.equals(s5));
    }
}