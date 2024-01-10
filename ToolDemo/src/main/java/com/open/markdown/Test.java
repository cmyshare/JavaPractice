package com.open.markdown;

import java.util.Arrays;
import java.util.List;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/10/25 18:03
 * @description
 */
public class Test {
    public static void main(String[] args) {
        String s1="sss ![image.png](https://cdn.nlark.com/yuque/0/2022/png/656655/1658055131305-ff1abc83-c5ce-4a2f-98f7-36ed78c6306c.png#clientId=ub8efe0f0-8178-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=273&id=u5f11bca9&margin=%5Bobject%20Object%5D&name=image.png&originHeight=273&originWidth=824&originalType=binary&ratio=1&rotation=0&showTitle=false&size=30255&status=done&style=none&taskId=u10a26e9e-5770-4588-9b69-35035e57aa9&title=&width=824)  sss";
        String s2="^[/#clientId].*[/)]$";
        String s = s1.replaceAll("#clientId.*[/)]", ")");
        System.out.println(s);


            Integer i=2;
            System.out.println(i.equals(2));
            System.out.println(2==i);

        List<Integer> splitLongIds = Arrays.asList(1,2,3,4,5,6,7,8,9);
        splitLongIds.forEach(f->{
            System.out.println("**************************"+f);
            if (f==5){
                System.out.println("**************************跳出数字5**************************************" +f);
                return;
            }

        });

    }
}
