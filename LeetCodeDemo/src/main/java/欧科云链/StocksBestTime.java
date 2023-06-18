package 欧科云链;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/9/16 14:47
 * @description 欧科云链-股票买卖最好时机
 */
public class StocksBestTime {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //价格数组长度n
        int n=sc.nextInt();
        //最多交易笔数k
        int k=sc.nextInt();
        //价格数组
        int[] prices=new int[n];
        for (int i = 0; i < n; i++) {
            prices[i]=sc.nextInt();
        }

        //收益数组
        int[] money=new int[n*n];
        //交易收益index
        int moneyIndex=0;
        for(int j=0;j<n-1;j++){
                if(prices[j+1]>prices[j]){
                    money[moneyIndex]=prices[j+1]-prices[j];
                    moneyIndex++;
                }

        }

        //排序 从小到大默认
        Arrays.sort(money);
        int sum=0;
        for(int m=1;m<=k;m++){
            sum=sum+money[money.length-m];
        }
        System.out.println(sum);
    }

}
