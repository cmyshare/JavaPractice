package 微众银行;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/9/14 18:19
 * @description 微众银行3-上升子序列
 */
public class AscendingSubsequence {
    /**
     * 深度优先搜索算法 （Depth First Search，简称DFS）：
     * 一种用于遍历或搜索树或图的算法。. 沿着树的深度遍历树的节点，尽可能深的搜索树的分支。
     *
     */
    static long ans = 0,mod = 998244353;
    static int[] dp;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n,m;
        n = sc.nextInt();
        m = sc.nextInt();
        dp = new int[n];
        //
        dfs(new int[n],0,n,m);
        System.out.println(ans%mod);
    }
    //深度优先搜索算法dfs
    public static void dfs( int[] nums,int index,int n,int m ) {
        // 求最长上升子序列
        int length = lis(nums,index);
        if ( length>3 )
            return;
        if ( n-index<3-length )
            return;
        if ( index==n ) {
            if ( length==3 )
                ans++;
            return;
        }

        for (int i = 1; i <= m; i++) {
            nums[index] = i;
            //递归往深处找
            dfs(nums,index+1,n,m);
        }
    }
    // 求最长上升子序列
    public static int lis( int[] nums,int cnt ) {
        if ( cnt<2 )
            return cnt;
        int res = 1;
        Arrays.fill(dp,1);
        for (int i = 1; i < cnt; i++) {
            for (int j = 0; j < i; j++) {
                if ( nums[i]>nums[j] ) {
                    dp[i] = Math.max(dp[j]+1,dp[i]);
                }
            }
            res = Math.max(res,dp[i]);
        }
        return res;
    }
}
