package TenSort;

import java.util.Arrays;

/**
 * @author CmyShare
 * @date 2025/4/9
 * @desc 1. 冒泡排序（Bubble Sort）
 */
public class BubbleSort1 {
    /**
     * 讲解
     * 冒泡排序通过多次比较相邻元素并交换它们的位置，将较大的元素逐步“冒泡”到数组末尾。
     * 每一轮都会确定一个最大值的位置。
     * 排序过程示例
     * 输入：[64, 34, 25, 12, 22, 11, 90]
     *
     * 第1轮：[34, 25, 12, 22, 11, 64, 90]
     * 第2轮：[25, 12, 22, 11, 34, 64, 90]
     * ...
     * 最终结果：[11, 12, 22, 25, 34, 64, 90]
     */
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 交换相邻元素
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        bubbleSort(arr);
        System.out.println("冒泡排序结果: " + Arrays.toString(arr));
    }
}

