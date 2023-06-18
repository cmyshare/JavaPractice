package 精灵云;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 搜索递增数列中的最后一个与目标值匹配的数，输出其下标，没有输出-1
 * 算法思想：
 * 1、获取vec有序数列的字符串形式
 * 2、采取二分法对数列进行目标搜索，当目标出现后把下标存入结果集，相当于小于目标值处理i=mid+1
 * 3、如果结果集为空，输出结果最后一个，反之输出-1。
 */
public class test2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //递增数列 以空格分隔每个数
        String s = scanner.nextLine();
        String[] split = s.split(" ");
        //结果集
        List<Integer> list = new ArrayList<>();
        //假定目标x=50
        int x= 50;
        int i = 0;
        int j = split.length-1;
        while (i <= j) {
            int mid=(i+j)/2;
            int mint = Integer.parseInt(split[mid]);
            //System.out.println("mid=" + mid);
            //System.out.println("mint=" + mint);
            if (mint==x){
                list.add(mid);
                i=mid+1;
            }else if(mint>x){
                j=mid-1;
            }else{
                i=mid+1;
            }
        }
        if (!list.isEmpty()){
            System.out.println(list.get(list.size()-1));
        }else{
            System.out.println("-1");
        }

    }
}
