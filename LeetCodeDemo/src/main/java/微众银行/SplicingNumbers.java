package 微众银行;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/9/14 17:12
 * @description 微众银行1-拼接数字
 */
public class SplicingNumbers {
    /**
     * 思路：用String[]存值，先初步长度和值排序，再取三个目标加入String[]再次字典序即可
     * 对于字符串，Arrays.sort默认采用字典序(ASCII)排序，先按首字符排序，如果首字符相同，再按第二个字符排序，以此类推。
     * Arrays.sort底层，长度小于47插入排序，小于286快速排序，大于286归并排序
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine(); //换行
        String s = sc.nextLine();
        //空格分片
        String[] nums = s.split(" ");

        //去除非法0开头的数字，题目已经保证数字范围，不可能0开头啊
        List<String> stringList = Arrays.stream(nums).filter(e -> e.charAt(0) > '0').collect(Collectors.toList());

        //使用Arrays的排序功能，并自定义排序器，按照长度和值从小到大
        Collections.sort(stringList, (o1, o2) -> {
            //位数相等时，比较值大小
            if ( o1.length()==o2.length() ) {
                return o1.compareTo(o2);
            } else {
                //位数不等时，直接比较长度，小于0升序，大于0降序
                return o1.length() - o2.length();
            }
        });
        //新的长度
        n=stringList.size();
        //定义最终数组
        String[] temp = new String[]{stringList.get(n-1),stringList.get(n-2),stringList.get(n-3)};
        //采用字典序(ASCII)排序+自定义排序
        Arrays.sort(temp,(o1,o2)->{
            //先长度然后首尾排序
            if ( o1.length()==o2.length() ) {
                //采用字典序(ASCII)排序
                return o1.compareTo(o2);
            }else{
                if(o1.charAt(0)==o2.charAt(0)){
                    //首部相等，处理尾部
                    return Character.compare(o1.charAt(o1.length()-1),o2.charAt(o2.length()-1));
                }else{
                    //首部不相等，处理首部
                    return Character.compare(o1.charAt(0),o2.charAt(0));
                }
            }
        });

        //返回结果
        StringBuilder ans = new StringBuilder();
        //倒着取三个数
        for (int i = temp.length - 1; i >= 0; i--) {
            //加入ans字符串末尾
            ans.append(temp[i]);
        }
        System.out.println(ans);
    }
}
