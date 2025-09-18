package TenSort;

import java.util.Arrays;

/**
 * @author CmyShare
 * @date 2025/4/9
 * @desc 4. 快速排序（Quick Sort）
 */
public class QuickSort4 {
    /**
     * 讲解
     * 通过选择一个基准值（pivot），将数组分为两部分：小于基准值的部分和大于基准值的部分。
     * 递归地对这两部分进行排序。
     * 排序过程示例
     * 输入：[10, 7, 8, 9, 1, 5]
     *
     * 第1轮：[5, 7, 8, 9, 1, 10]
     * 第2轮：[1, 5, 7, 8, 9, 10]
     * ...
     * 最终结果：[1, 5, 7, 8, 9, 10]
     * @param arr
     * @param low
     * @param high
     */
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                // 交换元素
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        // 将pivot放到正确位置
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    public static void main(String[] args) {
        int[] arr = {10, 7, 8, 9, 1, 5};
        quickSort(arr, 0, arr.length - 1);
        System.out.println("快速排序结果: " + Arrays.toString(arr));
    }
}