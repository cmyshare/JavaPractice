package TenSort;

import java.util.Arrays;

/**
 * @author CmyShare
 * @date 2025/4/9
 * @desc 2. 选择排序（Selection Sort）
 */
public class SelectionSort2 {
    /**
     * 讲解
     * 每次从未排序部分找到最小值，并将其放到已排序部分的末尾。
     * 排序过程示例
     * 输入：[64, 25, 12, 22, 11]
     *
     * 第1轮：[11, 25, 12, 22, 64]
     * 第2轮：[11, 12, 25, 22, 64]
     * ...
     * 最终结果：[11, 12, 22, 25, 64]
     * @param arr
     */
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            // 将最小值与当前元素交换
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 25, 12, 22, 11};
        selectionSort(arr);
        System.out.println("选择排序结果: " + Arrays.toString(arr));
    }
}