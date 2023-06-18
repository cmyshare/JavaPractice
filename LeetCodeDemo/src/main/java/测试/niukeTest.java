package 测试;

import java.util.Scanner;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/10/16 11:11
 * @description
 */
public class niukeTest {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String str1=in.nextLine();
        String str2=in.nextLine();
        StringBuffer stringBuffer1=new StringBuffer(str1);
        StringBuffer stringBuffer2=new StringBuffer(str2);
        //System.out.println(stringBuffer2.substring(0,2));
        //System.out.println();

        int num=0;
        for(int i=0;i<stringBuffer2.length()-1;i++){
            for(int j=0;j<stringBuffer2.length()-1;j++){
                if(stringBuffer1.charAt(i-1)==stringBuffer2.charAt(j-1)){
                    num++;
                }
                if(str1.contains(stringBuffer2.substring(0,i))){
                    num++;
                }
            }

        }
        System.out.println(num);
    }

}
