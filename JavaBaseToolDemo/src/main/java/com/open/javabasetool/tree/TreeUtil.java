package com.open.javabasetool.tree;

import java.util.*;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/8/30 15:02
 * @desc 树形结构转换工具类
 */
public class TreeUtil {

    /*************************************列表方案*********************************************/

    /**
     * 从平铺数据列表构建树形结构列表。
     *
     * @param flatList 平铺数据列表
     * @return 树形结构列表
     */
    public static List<TreeNode> buildTreeFromFlatListTwo(List<TreeNode> flatList) {
        Map<String, TreeNode> nodeMap = new HashMap<>();
        List<TreeNode> treeList = new ArrayList<>();

        // 将平铺数据转换为树节点并存储到映射表中
        for (TreeNode tiledData : flatList) {
            TreeNode node = new TreeNode();
            node.setId(tiledData.getId());
            node.setParentId(tiledData.getParentId());
            if ("0".equals(tiledData.getParentId())){
                node.setLevel(1);
                node.setSort(1);
            }
            node.setName(tiledData.getName());
            node.setExtendedFieldMap(tiledData.getExtendedFieldMap());
            nodeMap.put(node.getId(), node);
        }

        // 设置每个节点的父子关系
        for (TreeNode tiledData : flatList) {
            TreeNode node = nodeMap.get(tiledData.getId());
            if (node != null) {
                TreeNode parentNode = nodeMap.get(tiledData.getParentId());
                if (parentNode != null) {
                    if (parentNode.getChildrenList() == null) {
                        parentNode.setChildrenList(new ArrayList<>());
                    }
                    parentNode.getChildrenList().add(node);
                    node.setLevel(parentNode.getLevel() + 1);
                } else {
                    // 如果没有父节点，则认为是根节点
                    treeList.add(node);
                }
            }
        }

        return treeList;
    }

    /**
     * 将树形结构列表转换为平铺数据列表。
     *
     * @param nodeList 树形结构列表
     * @return 平铺数据列表
     */
    public static List<TreeNode> flattenTreeToListTwo(List<TreeNode> nodeList) {
        List<TreeNode> flatList = new ArrayList<>();
        for (TreeNode node : nodeList) {
            flattenTreeToListTwo(node, flatList);
        }

        //填充排序字段
        for (int i = 0; i < flatList.size(); i++) {
            TreeNode tiledData = flatList.get(i);
            tiledData.setSort(i);
        }

        return flatList;
    }

    private static void flattenTreeToListTwo(TreeNode node, List<TreeNode> flatList) {
        if (node != null) {
            TreeNode tiledData = new TreeNode();
            tiledData.setId(node.getId());
            tiledData.setParentId(node.getParentId());
            tiledData.setName(node.getName());
            tiledData.setExtendedFieldMap(node.getExtendedFieldMap());
            tiledData.setLevel(node.getLevel());
            tiledData.setSort(node.getSort());
            flatList.add(tiledData);

            if (node.getChildrenList() != null) {
                for (TreeNode child : node.getChildrenList()) {
                    flattenTreeToListTwo(child, flatList);
                }
            }
        }
    }

}
