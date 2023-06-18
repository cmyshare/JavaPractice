package 是360;

import java.util.Scanner;

/**
 * 题目1-复制粘贴
 * 注意输入输出的格式，看结果是否需要装在一起输出？
 * 重新StringBuilder长度：setLength(长度)
 * 在StringBuilder指定坐标后加入字符：insert(下标，元素)
 * 截取字符串substring(开始，结束不包括)
 */
public class Test1 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        //组数
        int nums = sc.nextInt();
        //换行
        sc.nextLine();
        boolean[] s=new boolean[nums];
        for (int i=0;i<nums;i++){
            String source = sc.nextLine();
            String target = sc.nextLine();
            boolean result=copyHandle(source,target);
            s[i]=result;
        }

        for(int b=0;b<s.length;b++){
            System.out.println(s[b]);
        }
    }

    //给她一个字符串s，她复制其中的一个字符并将其粘贴到这个字符的下一位，将s转成目标t
    private static boolean copyHandle(String source, String target) {
        //直接相等
        if(source.equals(target)){
            return true;
        }
        //来源大于目标长度
        if(source.length()>=target.length()){
            return false;
        }
        StringBuilder sources=new StringBuilder(source);
        //扩大source数组长度
        sources.setLength(target.length());
        StringBuilder targets=new StringBuilder(target);

        //判断第一个是否相等
        if(sources.charAt(0)!=targets.charAt(0)){
            return false;
        }

        //从第2个比较
        int sourceIndex=1;
        int j=1;
        while (j<targets.length()){
            if(targets.charAt(j)==sources.charAt(sourceIndex)){
                sourceIndex++;
                j++;
                continue;
            }else{
                //如果不等时，判断targets当前字符==sources前一个字符
                if(targets.charAt(j)==sources.charAt(sourceIndex-1)){
                    //在sources前一个字符后加入targets当前字符
                    sources.insert(sourceIndex-1,targets.charAt(j));
                }else{
                   return false;
                }
            }
        }
        //截取targets.length()长度比较
        String s1=sources.substring(0,targets.length());
        String s2=targets.toString();
        return s1.equals(s2);
    }
}
