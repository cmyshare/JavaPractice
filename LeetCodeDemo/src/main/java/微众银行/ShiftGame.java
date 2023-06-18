package 微众银行;

import java.util.Scanner;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/9/14 17:12
 * @description 微众银行2-移位游戏
 */
public class ShiftGame {
    /**
     * 转换有6种方式：乘除2、4、8。值为2的次方才能被转换成功
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //多少组数据
        int n = sc.nextInt();
        //a开始 b目标
        long a, b;
        while (n-- > 0) {
            //默认转换次数为Integer最大值
            int ans = Integer.MAX_VALUE;
            a = sc.nextLong();
            b = sc.nextLong();
            //统一y小x大
            long x = Math.max(a, b);
            long y = Math.min(a, b);
            //是否相等
            if (x == y)
                ans = 0;
                // 判断能否整除
            else if (x % y != 0)
                ans = -1;
            else {
                //方法1 每次判断除以允许值，对比目标值
                ans=0;
                while (y<x){
                    if(y*8 <= x){
                        y *=8;
                    }else if(y*4 <=x){
                        y *= 4;
                    }else{
                        y *= 2;
                    }
                    ans++;
                }
                //判断x是否是y的2次方
                if(y!=x){
                    ans=-1;
                }

                //方法2 求整除2次次数，再进行浓缩得出ans
                //// 判断x是否是y的2次方
                //long t = x / y;
                ////t整除2的次数
                //int cnt = 0;
                //while (t > 1) {
                //    if (t % 2 == 1) {
                //        ans = -1;
                //        break;
                //    }
                //    //512 256 128 64 32 16 8 4 2 1
                //    t /= 2;
                //    //1 2 3 4 5 6 7 8 9 10
                //    cnt++;
                //}
                ////浓缩计算转换次数，根据允许乘除数字的个数
                //if (ans != -1) {
                //    ans = 0;
                //    //数字3
                //    ans += cnt / 3;
                //    cnt %= 3;
                //    //数字2
                //    ans += cnt / 2;
                //    cnt %= 2;
                //    //数字1
                //    ans += cnt;
                //}
            }
            //输出转换次数，不能输出-1
            System.out.println(ans);
        }
    }
}
