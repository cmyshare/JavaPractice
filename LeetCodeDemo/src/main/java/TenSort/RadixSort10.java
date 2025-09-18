package TenSort;

import java.util.Arrays;

/**
 * @author CmyShare
 * @date 2025/4/9
 * @desc 10. 基数排序（Radix Sort）
 */
public class RadixSort10 {
    /**
     * 10. 基数排序（Radix Sort）
     * 讲解
     * 基数排序也是一种非比较型排序算法，适用于整数排序。它从最低位开始对每一位进行排序（通常使用稳定排序算法如计数排序），逐位处理直至最高位。
     * 这种方法允许我们在不直接比较元素的情况下对它们进行排序。
     * 排序过程示例
     * 输入：[170, 45, 75, 90, 802, 24, 2, 66]
     * <p>
     * 首先按个位数排序：[170, 90, 802, 2, 24, 45, 75, 66]
     * 然后按十位数排序：[802, 2, 24, 45, 66, 170, 75, 90]
     * 最后按百位数排序：[2, 24, 45, 66, 75, 90, 170, 802]
     * 最终结果：[2, 24, 45, 66, 75, 90, 170, 802]
     *
     * @param arr
     */
    public static void radixSort(int[] arr) {
        int max = Arrays.stream(arr).max().getAsInt();
        for (int exp = 1; max / exp > 0; exp *= 10) countingSortByDigit(arr, exp);
    }

    private static void countingSortByDigit(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];
        for (int num : arr) count[(num / exp) % 10]++;
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];
        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }
        System.arraycopy(output, 0, arr, 0, n);
    }

    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        radixSort(arr);
        System.out.println("基数排序结果: " + Arrays.toString(arr));
    }
}

