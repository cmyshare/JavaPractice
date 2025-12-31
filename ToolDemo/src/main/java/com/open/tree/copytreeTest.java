package com.open.tree;

import java.util.Arrays;
import java.util.List;

/**
 * @author CmyShare
 * @date 2026/1/1
 */
public class copytreeTest {
    public static void main(String[] args) {
        // 模拟四层 OKR 树（根 → L1 → L2 → L3）
        List<HrmsOkrInfo> sourceList = Arrays.asList(
                // Level 0: 根节点（parentId == id）
                new HrmsOkrInfo("1", "1", "0", 0, "公司年度目标"),

                // Level 1
                new HrmsOkrInfo("2", "1", "0,1", 0, "Q1 目标"),

                // Level 2
                new HrmsOkrInfo("3", "2", "0,1,2", 0, "部门OKR"),
                new HrmsOkrInfo("4", "2", "0,1,2", 0, "个人OKR"),

                // Level 3
                new HrmsOkrInfo("5", "3", "0,1,2,3", 0, "KR1: 提升系统性能"),
                new HrmsOkrInfo("6", "3", "0,1,2,3", 0, "KR2: 优化用户体验"),
                new HrmsOkrInfo("7", "4", "0,1,2,4", 0, "KR: 完成培训")
        );

        System.out.println("=== 原始数据 ===");
        sourceList.forEach(System.out::println);

        // 调用复制工具（使用你前面定义的 TreeCopyUtils）
        List<HrmsOkrInfo> newList = TreeUtil.copyOkrTree(sourceList);

        System.out.println("\n=== 复制后的新数据 ===");
        newList.forEach(System.out::println);

        // 验证数量
        assert newList.size() == 7 : "数量不一致！";
        assert newList.stream().allMatch(o -> o.getStatus() == 0) : "status 不为 0！";

        // 打印新树结构（按 ancestors 排序便于观察）
        System.out.println("\n=== 按层级排序的新树 ===");
        newList.stream()
                .sorted((a, b) -> a.getAncestors().length() - b.getAncestors().length())
                .forEach(System.out::println);
    }
}
