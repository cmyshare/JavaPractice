package hot100;

import java.util.*;

/**
 * @author CmyShare
 * @date 2025/4/16
 * @description 字母异位词分组
 */
public class groupAnagrams2 {
    /**
     * 字母异位词分组
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        //key为单词字母，value为单词列表
        Map<String,List<String>> listMap=new HashMap<>();

        for(String str:strs){
            //将单词转换为字符数组
            char[] chars=str.toCharArray();
            //对字符数组进行排序
            Arrays.sort(chars);
            //将排序后的字符数组转换为字符串
            String key=String.valueOf(chars);

            //判断是否存在key
            if (listMap.containsKey(key)){
                boolean add = listMap.get(key).add(str);
            }else{
                //不存在key，创建新的list
                List<String> stringsOne=new ArrayList<>();
                stringsOne.add(str);
                listMap.put(key, stringsOne);
            }
        }

        List<List<String>> resultList = new ArrayList<>(listMap.values());
        return resultList;
    }
}
