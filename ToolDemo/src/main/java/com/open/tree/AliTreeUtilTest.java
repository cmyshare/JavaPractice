package com.open.tree;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author CmyShare
 * @date 2026/1/1
 */
public class AliTreeUtilTest {
    public static void main(String[] args) {
        List<AliMenu> menuList = Arrays.asList(
                new AliMenu(1L, 0L, "系统管理"),
                new AliMenu(2L, 0L, "用户管理"),
                new AliMenu(3L, 1L, "菜单管理"),
                new AliMenu(4L, 1L, "角色管理"),
                new AliMenu(5L, 2L, "用户列表"),
                new AliMenu(6L, 3L, "菜单新增"),
                new AliMenu(7L, 3L, "菜单编辑")
        );

        System.out.println("==========================3.1 makeTree：构建树==============================");
        //🧪 3.1 makeTree：构建树
        List<AliMenu> tree = AliTreeUtil.makeTree(
                menuList,
                AliMenu::getParentId,     // pIdGetter
                AliMenu::getId,           // idGetter
                menu -> menu.getParentId() == 0L, // rootCheck
                AliMenu::setSubMenus      // setSubChildren
        );
        // 打印结果
        //tree.forEach(menu -> System.out.println(menu.getName()));
        printTree(tree, 0);

        System.out.println("==========================3.1 makeTree：树转列表==============================");
        List<AliMenu> flatList = AliTreeUtil.flattenTreeDFS(tree, AliMenu::getSubMenus);
        System.out.println("DFS深度优先遍历展平结果：");
        flatList.forEach(m -> System.out.println(m.getName()));

        List<AliMenu> flattenTreeDFSIterative = AliTreeUtil.flattenTreeDFSIterative(tree, AliMenu::getSubMenus);
        System.out.println("DFS深度优先非递归，显式栈遍历展平结果：");
        flattenTreeDFSIterative.forEach(m -> System.out.println(m.getName()));

        // BFS 展平
        List<AliMenu> flatBFS = AliTreeUtil.flattenTreeBFS(tree, AliMenu::getSubMenus);
        System.out.println("BFS广度优先遍历展平结果：");
        flatBFS.forEach(m -> System.out.println(m.getName()));

        System.out.println("==========================3.1 makeTree：树转列表 设置 depth & ancestors==============================");
        List<AliMenu> flatList2 = AliTreeUtil.flattenTreeDFSWithDepthAndAncestors(tree, AliMenu::getSubMenus, AliMenu::getId, AliMenu::setDepth, AliMenu::setAncestors);
        System.out.println("DFS深度优先遍历展平 设置 depth & ancestors结果：");
        flatList2.forEach(System.out::println);

        List<AliMenu> flattenTreeDFSIterative2 = AliTreeUtil.flattenTreeDFSIterativeWithDepthAndAncestors(tree, AliMenu::getSubMenus, AliMenu::getId, AliMenu::setDepth, AliMenu::setAncestors);
        System.out.println("DFS深度优先非递归，显式栈遍历展平 设置 depth & ancestors结果：");
        flattenTreeDFSIterative2.forEach(System.out::println);

        // BFS 展平
        List<AliMenu> flatBFS2 = AliTreeUtil.flattenTreeBFSWithDepthAndAncestors(tree, AliMenu::getSubMenus, AliMenu::getId, AliMenu::setDepth, AliMenu::setAncestors);
        System.out.println("BFS广度优先遍历展平 设置 depth & ancestors结果：");
        flatBFS2.forEach(System.out::println);

        System.out.println("==========================3.2 search：树中查找（保留匹配节点及其路径）==============================");
        //🧪 3.2 search：树中查找（保留匹配节点及其路径）
        List<AliMenu> searched = AliTreeUtil.search(
                tree,
                menu -> menu.getName().contains("菜单管理"),
                AliMenu::getSubMenus
        );
        // 打印
        printTree(searched, 0);

        System.out.println("==========================3.3 filter：严格过滤（只保留完全匹配的节点及其匹配的子孙）==============================");
        //🧪 3.3 filter：严格过滤（只保留完全匹配的节点及其匹配的子孙）
        List<AliMenu> filtered = AliTreeUtil.filter(
                tree,
                menu -> menu.getName().contains("用户管理"),
                AliMenu::getSubMenus
        );

        printTree(filtered, 0);

        System.out.println("==========================3.4 sort：对树按名称排序==============================");
        //🧪 3.4 sort：对树按名称排序
        AliTreeUtil.sort(
                tree,
                Comparator.comparing(AliMenu::getName),
                AliMenu::getSubMenus
        );

        printTree(tree, 0);

        System.out.println("==========================3.5 filterAndHandler：过滤并设置字段（如 choose）==============================");
        //🧪 3.5 filterAndHandler：过滤并设置字段（如 choose）
        AliTreeUtil.filterAndHandler(
                tree,
                menu -> menu.getName().contains("用户"),
                AliMenu::getSubMenus,
                AliMenu::setChoose
        );
        // 验证 choose 值
        validateChoose(tree);


        System.out.println("==========================重建树形结构，生成新的 Long 类型 ID（最常见场景）==============================");
        // 原始展平数据（模拟从数据库查出）
        List<AliMenu> originalFlat = Arrays.asList(
                new AliMenu(1L, 0L, "0", "系统管理"),          // 根节点：ancestors=0
                new AliMenu(2L, 0L, "0", "用户管理"),          // 根节点：ancestors=0
                new AliMenu(3L, 1L, "0,1", "菜单管理"),        // 父ID=1：ancestors=0,1
                new AliMenu(4L, 1L, "0,1", "角色管理"),        // 父ID=1：ancestors=0,1
                new AliMenu(5L, 2L, "0,2", "用户列表"),        // 父ID=2：ancestors=0,2
                new AliMenu(6L, 3L, "0,1,3", "菜单新增"),      // 父ID=3：ancestors=0,1,3
                new AliMenu(7L, 3L, "0,1,3", "菜单编辑")       // 父ID=3：ancestors=0,1,3
        );

        System.out.println("=== 原始数据 ===");
        originalFlat.forEach(System.out::println);

        // 重建树
        //List<AliMenu> regenerated = AliTreeUtil.regenerateTreeWithNewLongIds(
        //        originalFlat);

        List<AliMenu> regenerated = AliTreeUtil.regenerateTreeWithNewLongIds(
                originalFlat,
                AliMenu::getId,
                AliMenu::setId,
                AliMenu::getParentId,
                AliMenu::setParentId,
                AliMenu::getAncestors,
                AliMenu::setAncestors,
                () -> IdUtil.getSnowflake().nextId() // 自定义ID生成
        );

        System.out.println("\n=== 重建后数据（新ID/parentId/ancestors） ===");
        regenerated.forEach(System.out::println);
    }

    /**
     * 打印树结构（递归实现）
     * @param menus 树节点列表
     * @param level 当前递归层级（初始为0）
     */
    public static void printTree(List<AliMenu> menus, int level) {
        for (AliMenu m : menus) {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < level; i++) {
                indent.append("  ");
            }
            System.out.println(indent.toString() + m.getName());
            if (m.getSubMenus() != null && !m.getSubMenus().isEmpty()) {
                printTree(m.getSubMenus(), level + 1);
            }
        }
    }

    /**
     * 验证树节点的 choose 值（递归实现）
     * @param menus 树节点列表
     */
    public static void validateChoose(List<AliMenu> menus) {
        for (AliMenu m : menus) {
            System.out.println(m.getName() + " -> choose=" + m.getChoose());
            if (m.getSubMenus() != null) {
                validateChoose(m.getSubMenus());
            }
        }
    }

}
