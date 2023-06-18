package 精灵云;

import java.util.Scanner;

/**
 * 逆转英文句子中的单词
 * 算法思想：
 * 1、读取输入字符串，创建结果输出集result
 * 2、将字符串中.符号替换成!后进行句子分割
 * 3、遍历句子数组，去除结尾符.通过StringBuilder的reverse逆转方法处理单个句子，逆转后加入句子结尾符.
 * 4、输出结果集
 */
public class test1 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String s = scanner.nextLine();
        //如果s不为空
        if (!s.isEmpty()){
            //输出字符串
            StringBuilder result=new StringBuilder();
            //获取多少个句
            s=s.replace(".","!");
            String[] splits = s.split("!");
            //倒序循环，对单个String倒序处理
            for (String split : splits) {
                //System.out.println("deal-----"+split);
                //去掉结尾.符号 倒转一句话单词
                StringBuilder stringBuilder=new StringBuilder(split);
                result.append(stringBuilder.reverse());
                //每句话以.结尾
                result.append(".");
            }
            System.out.println(result);
        }
    }
}
