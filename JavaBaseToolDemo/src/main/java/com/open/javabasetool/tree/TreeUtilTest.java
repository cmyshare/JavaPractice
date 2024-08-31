//package com.open.javabasetool.tree;
//
//import com.alibaba.fastjson2.JSON;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * @version 1.0
// * @Author cmy
// * @Date 2024/8/30 15:19
// * @desc 树形工具测试类
// */
//
//public class TreeUtilTest {
//    public static void main(String[] args) {
//        List<TreeNode> TreeNodeList = new ArrayList<>();
//
//        // 创建一些示例数据
//        TreeNode root1 = new TreeNode(1L, 0L, "Root 1",0,0, new HashMap<>());
//        TreeNode child1_1 = new TreeNode(2L, 1L, "Child 1.1",0,0, new HashMap<>());
//        TreeNode child1_2 = new TreeNode(3L, 1L, "Child 1.2",0,0, new HashMap<>());
//        TreeNode grandchild1_2_1 = new TreeNode(4L, 3L, "Grandchild 1.2.1",0,0, new HashMap<>());
//
//        TreeNode root2 = new TreeNode(5L, 0L, "Root 2",0,0, new HashMap<>());
//        TreeNode child2_1 = new TreeNode(6L, 5L, "Child 2.1",0,0, new HashMap<>());
//        TreeNode child2_2 = new TreeNode(7L, 5L, "Child 2.2",0,0, new HashMap<>());
//        TreeNode grandchild2_2_1 = new TreeNode(8L, 7L, "Grandchild 2.2.1",0,0, new HashMap<>());
//        // 添加到列表中
//        TreeNodeList.add(root1);
//        TreeNodeList.add(child1_1);
//        TreeNodeList.add(child1_2);
//        TreeNodeList.add(grandchild1_2_1);
//        TreeNodeList.add(root2);
//        TreeNodeList.add(child2_1);
//        TreeNodeList.add(child2_2);
//        TreeNodeList.add(grandchild2_2_1);
//
//        List<TreeNode> treeNodes = TreeUtil.buildTreeFromFlatListTwo(TreeNodeList);
//        System.out.println("================================"+ JSON.toJSONString(treeNodes));
//
//
//        List<TreeNode> treeNodeList=new ArrayList<>();
//        // 创建一些示例数据
//        TreeNode root11 = new TreeNode(1L, 0L, "Root 1",1,1,new ArrayList<>(), new HashMap<>());
//        TreeNode child11_1 = new TreeNode(2L, 1L, "Child 1.1",2,2,new ArrayList<>(), new HashMap<>());
//        TreeNode child11_2 = new TreeNode(3L, 1L, "Child 1.2", 2,3,new ArrayList<>(), new HashMap<>());
//        TreeNode grandchild11_2_1 = new TreeNode(4L, 3L, "Grandchild 1.2.1", 3,4,new ArrayList<>(),new HashMap<>());
//
//        TreeNode root21 = new TreeNode(5L, 0L, "Root 2", 1,1,new ArrayList<>(), new HashMap<>());
//        TreeNode child21_1 = new TreeNode(6L, 5L, "Child 2.1", 2,2,new ArrayList<>(), new HashMap<>());
//        TreeNode child21_2 = new TreeNode(7L, 5L, "Child 2.2", 2,3,new ArrayList<>(), new HashMap<>());
//        TreeNode grandchild21_2_1 = new TreeNode(8L, 7L, "Grandchild 2.2.1", 2,4,new ArrayList<>(), new HashMap<>());
//        // 添加到列表中
//        treeNodeList.add(root11);
//        treeNodeList.add(child11_1);
//        treeNodeList.add(child11_2);
//        treeNodeList.add(grandchild11_2_1);
//        treeNodeList.add(root21);
//        treeNodeList.add(child21_1);
//        treeNodeList.add(child21_2);
//        treeNodeList.add(grandchild21_2_1);
//
//        List<TreeNode> TreeNode = TreeUtil.flattenTreeToListTwo(treeNodeList);
//        System.out.println("================================"+ JSON.toJSONString(TreeNode));
//    }
//}
