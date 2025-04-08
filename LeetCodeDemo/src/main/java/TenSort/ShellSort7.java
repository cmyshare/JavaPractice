package TenSort;

import java.util.Arrays;

/**
 * @author CmyShare
 * @date 2025/4/9
 * @desc 7. 希尔排序（Shell Sort）
 */
public class ShellSort7 {
    /**
     * 7. 希尔排序（Shell Sort）
     * 讲解
     * 希尔排序是插入排序的一种改进版本，它通过比较相隔一定间隔的元素来排序数组。
     * 初始时选择较大的间隔，随后逐步减小间隔，直到间隔为1时进行最后一次插入排序。
     * 排序过程示例
     * 输入：[12, 11, 13, 5, 6, 7]
     *
     * 设定初始间隔为3，分成子序列排序：[12, 5], [11, 6], [13, 7]
     * 缩小间隔到1，执行最终插入排序。
     * 中间步骤省略，最终结果：[5, 6, 7, 11, 12, 13]
     * @param arr
     */
    public static void shellSort(int[] arr) {
        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j;
                for (j = i; j >= gap && arr[j - gap] > temp; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = temp;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        shellSort(arr);
        System.out.println("希尔排序结果: " + Arrays.toString(arr));
    }
}

