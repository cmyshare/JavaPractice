package TenSort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author CmyShare
 * @date 2025/4/9
 * @desc 9. 桶排序（Bucket Sort）
 */
public class BucketSort9 {
    /**
     * 9. 桶排序（Bucket Sort）
     * 讲解
     * 桶排序是一种分布式的排序算法，主要思想是将要排序的数据分到有限数量的“桶”里，每个桶内的数据再单独进行排序。
     * 适合于数据均匀分布的情况。
     * 排序过程示例
     * 输入：[0.78, 0.17, 0.39, 0.26, 0.72, 0.94, 0.21, 0.12, 0.23, 0.68]
     *
     * 将数据分配到不同桶中，如分为10个桶，根据数值范围。
     * 对每个桶内部的数据进行排序。
     * 合并所有桶的数据得到最终排序结果。
     * 最终结果：[0.12, 0.17, 0.21, 0.23, 0.26, 0.39, 0.68, 0.72, 0.78, 0.94]
     * @param arr
     */
    public static void bucketSort(int[] arr) {
        int n = arr.length;
        List<Integer>[] buckets = new List[n];
        for (int i = 0; i < n; i++) buckets[i] = new ArrayList<>();
        for (int num : arr) buckets[hash(num, n)].add(num);
        for (List<Integer> bucket : buckets) Collections.sort(bucket);
        int index = 0;
        for (List<Integer> bucket : buckets) {
            for (int num : bucket) arr[index++] = num;
        }
    }

    private static int hash(int num, int n) {
        return num / n;
    }

    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        bucketSort(arr);
        System.out.println("桶排序结果: " + Arrays.toString(arr));
    }
}

