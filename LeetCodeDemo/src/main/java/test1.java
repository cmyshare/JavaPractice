//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * @version 1.0
// * @Author cmy
// * @Date 2023/12/13 14:32
// * @desc
// */
//public class test1 {
//   /**
//    * 一个工厂的生产车间分别有四个部门，四个部门为A、B、C、D。在这些部门工作的工人满足以下条件：每个部门只有3个人，这个生产车间任意每两个工人至少一起在一个部门工作。
//    * Java编写一段程序，计算该生产车间最多可以有多少(工人并生成所有符合上述条件的)分组可能？
//    *
//    * 123 456 789 101112
//    *
//    * 124 563 7810 11129
//    */
//
//   public static void main(String[] args) {
//       int maxWorkers = 3;  // 初始每个部门有3个工人
//       boolean found = false;
//
//       while (!found) {
//           List<List<Integer>> allCombinations = new ArrayList<>();
//           for (int[] combination : combinations(maxWorkers, 3)) {
//               List<Integer> combinationList = Arrays.stream(combination).boxed().collect(Collectors.toList());
//               allCombinations.add(combinationList);
//           }
//
//           for (int i = 0; i < maxWorkers; i++) {
//               for (int j = i + 1; j < maxWorkers; j++) {
//                   if (!isPairInSameGroup(allCombinations, i, j)) {
//                       found = true;
//                       break;
//                   }
//               }
//               if (found) {
//                   break;
//               }
//           }
//           if (found) {
//               break;
//           }
//           maxWorkers++;
//       }
//
//       List<List<List<Integer>>> validGroups = new ArrayList<>();
//       for (List<Integer> combination : combinations(maxWorkers, 4)) {
//           if (combination.size() == maxWorkers) {
//               validGroups.add(combination);
//           }
//       }
//
//       System.out.println("Max Workers: " + maxWorkers);
//       System.out.println("Valid Groups: " + validGroups.size());
//   }
//
//    private static boolean isPairInSameGroup(List<List<Integer>> combinations, int worker1, int worker2) {
//        for (List<Integer> combination : combinations) {
//            if (combination.contains(worker1) && combination.contains(worker2)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static List<Integer> combinations(int n, int k) {
//        List<int[]> result = new ArrayList<>();
//        int[] current = new int[k];
//        backtrack(result, current, 0, n - k + 1, k);
//        return result;
//    }
//
//    private static void backtrack(List<int[]> result, int[] current, int start, int n, int k) {
//        if (k == 0) {
//            result.add(current);
//            return;
//        }
//        for (int i = start; i <= n - k + 1; i++) {
//            current[k - 1] = i;
//            backtrack(result, current, i + 1, n, k - 1);
//        }
//    }
//
//}
