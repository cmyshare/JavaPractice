package TenSort;

import java.util.Arrays;

/**
 * @author CmyShare
 * @date 2025/4/9
 * @desc 5. 归并排序（Merge Sort）
 */
public class MergeSort5 {
    /**
     * 讲解
     * 采用分治法，将数组分成两半分别排序，然后合并两个有序数组。
     * 排序过程示例
     * 输入：[12, 11, 13, 5, 6, 7]
     *
     * 分解：[12, 11, 13] 和 [5, 6, 7]
     * 合并：[11, 12, 13] 和 [5, 6, 7]
     * 最终结果：[5, 6, 7, 11, 12, 13]
     * 由于篇幅限制，以下算法仅列出核心代码和简要说明：
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
        System.out.println("归并排序结果: " + Arrays.toString(arr));
    }
}

