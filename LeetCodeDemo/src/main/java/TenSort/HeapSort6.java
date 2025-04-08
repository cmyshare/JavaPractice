package TenSort;

import java.util.Arrays;

/**
 * @author CmyShare
 * @date 2025/4/9
 * @desc 6. 堆排序（Heap Sort）
 */
public class HeapSort6 {
    /**
     * 讲解
     * 堆排序是一种基于二叉堆数据结构的比较排序算法。
     * 它首先将数组构造成一个最大堆，然后依次取出根节点（最大值），并将最后一个元素放到根位置，再调整堆，重复此过程直到排序完成。
     * 排序过程示例
     * 输入：[12, 11, 13, 5, 6, 7]
     *
     * 构建最大堆：[13, 11, 12, 5, 6, 7]
     * 取出最大值并调整堆：
     * [12, 11, 7, 5, 6]
     * [11, 6, 7, 5]
     * [7, 6, 5]
     * [6, 5]
     * [5]
     * 最终结果：[5, 6, 7, 11, 12, 13]
     * @param arr
     */
    public static void heapSort(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i, left = 2 * i + 1, right = 2 * i + 2;
        if (left < n && arr[left] > arr[largest]) largest = left;
        if (right < n && arr[right] > arr[largest]) largest = right;
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest);
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        heapSort(arr);
        System.out.println("堆排序结果: " + Arrays.toString(arr));
    }
}

