package TenSort;

import java.util.Arrays;

/**
 * @author CmyShare
 * @date 2025/4/9
 * @desc 3. 插入排序（Insertion Sort）
 */
public class InsertionSort3 {
    /**
     * 讲解
     * 将未排序部分的元素逐个插入到已排序部分的正确位置。
     * 排序过程示例
     * 输入：[12, 11, 13, 5, 6]
     *
     * 第1轮：[11, 12, 13, 5, 6]
     * 第2轮：[11, 12, 13, 5, 6]
     * ...
     * 最终结果：[5, 6, 11, 12, 13]
     * @param arr
     */
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public static void main(String[] args) {
        int[] arr = {12, 11, 13, 5, 6};
        insertionSort(arr);
        System.out.println("插入排序结果: " + Arrays.toString(arr));
    }
}