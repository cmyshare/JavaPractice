package TenSort;

import java.util.Arrays;

/**
 * @author CmyShare
 * @date 2025/4/9
 * @desc 8. 计数排序（Counting Sort）
 */
public class CountingSort8 {
    /**
     * 8. 计数排序（Counting Sort）
     * 讲解
     * 计数排序适用于整数排序，特别是当数值范围较小时更为有效。
     * 它不是基于比较的排序算法，而是通过计算每个值出现的次数来确定每个元素在输出数组中的位置。
     * 排序过程示例
     * 输入：[4, 2, 2, 8, 3, 3, 1]
     *
     * 统计每个数字出现的次数。
     * 根据统计结果放置每个数字到正确的位置。
     * 最终结果：[1, 2, 2, 3, 3, 4, 8]
     * @param arr
     */
    public static void countingSort(int[] arr) {
        int max = Arrays.stream(arr).max().getAsInt();
        int[] count = new int[max + 1];
        for (int num : arr) count[num]++;
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            while (count[i]-- > 0) arr[index++] = i;
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        countingSort(arr);
        System.out.println("计数排序结果: " + Arrays.toString(arr));
    }
}

