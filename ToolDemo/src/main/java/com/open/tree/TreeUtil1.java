package com.open.tree;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;

import java.util.*;
import java.util.function.Supplier;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/8/30 15:02
 * @desc 树形结构转换工具类
 */
public class TreeUtil1 {

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


    /**
     * 复制树形结构（通用）
     *
     * @param sourceList       源列表（必须包含 id, parentId）
     * @param entitySupplier   新实体构造器（如 HrmsOkrInfo::new）
     * @param idField          ID 字段名（默认 "id"）
     * @param parentIdField    父ID字段名（默认 "parentId"）
     * @param ancestorsField   祖先路径字段名（默认 "ancestors"）
     * @param statusField      状态字段名（默认 "status"）
     * @param <T>              实体类型
     * @return 新树列表
     */
    public static <T> List<T> copyTree(
            List<T> sourceList,
            Supplier<T> entitySupplier,
            String idField,
            String parentIdField,
            String ancestorsField,
            String statusField) {

        if (CollUtil.isEmpty(sourceList)) {
            return Collections.emptyList();
        }

        // 1. 构建 oldId -> entity 映射
        Map<Object, T> oldIdToEntity = new HashMap<>(sourceList.size());
        for (T item : sourceList) {
            Object id = ReflectUtil.getFieldValue(item, idField);
            oldIdToEntity.put(id, item);
        }

        // 2. 构建父子关系图（childOldId -> parentOldId）
        Map<Object, Object> childToParent = new HashMap<>(sourceList.size());
        Set<Object> allIds = new HashSet<>(oldIdToEntity.keySet());

        for (T item : sourceList) {
            Object id = ReflectUtil.getFieldValue(item, idField);
            Object parentId = ReflectUtil.getFieldValue(item, parentIdField);

            // 判断是否为根：parentId == id（自引用）或 parentId 不在当前树中
            if (Objects.equals(parentId, id) || !allIds.contains(parentId)) {
                childToParent.put(id, "ROOT"); // 标记为根
            } else {
                childToParent.put(id, parentId);
            }
        }

        // 3. 生成 oldId -> newId 映射（顺序无关，但需确定性）
        Map<Object, String> oldIdToNewId = new HashMap<>(sourceList.size());
        for (Object oldId : allIds) {
            oldIdToNewId.put(oldId, IdUtil.getSnowflakeNextIdStr());
        }

        // 4. 构建 newId -> newAncestors 映射（BFS 层序遍历）
        Map<String, String> newIdToAncestors = new HashMap<>(sourceList.size());
        Queue<Object> queue = new LinkedList<>();
        Map<String, String> newIdToNewParentId = new HashMap<>(sourceList.size());

        // 入队所有根节点
        for (Map.Entry<Object, Object> entry : childToParent.entrySet()) {
            if ("ROOT".equals(entry.getValue())) {
                queue.offer(entry.getKey());
                String newId = oldIdToNewId.get(entry.getKey());
                newIdToAncestors.put(newId, "0");
                newIdToNewParentId.put(newId, "0");
            }
        }

        // BFS 构建 ancestors
        while (!queue.isEmpty()) {
            Object oldId = queue.poll();
            String newId = oldIdToNewId.get(oldId);
            String currentAncestors = newIdToAncestors.get(newId);

            // 查找所有子节点
            for (Map.Entry<Object, Object> entry : childToParent.entrySet()) {
                Object childOldId = entry.getKey();
                Object parentOldId = entry.getValue();

                if (Objects.equals(parentOldId, oldId)) {
                    String childNewId = oldIdToNewId.get(childOldId);
                    newIdToNewParentId.put(childNewId, newId);
                    newIdToAncestors.put(childNewId, currentAncestors + "," + newId);
                    queue.offer(childOldId);
                }
            }
        }

        // 5. 构建最终结果列表
        List<T> result = new ArrayList<>(sourceList.size());
        for (T source : sourceList) {
            Object oldId = ReflectUtil.getFieldValue(source, idField);
            String newId = oldIdToNewId.get(oldId);

            T target = entitySupplier.get();
            BeanUtil.copyProperties(source, target);

            ReflectUtil.setFieldValue(target, idField, newId);
            ReflectUtil.setFieldValue(target, statusField, 0);
            ReflectUtil.setFieldValue(target, parentIdField, newIdToNewParentId.get(newId));
            ReflectUtil.setFieldValue(target, ancestorsField, newIdToAncestors.get(newId));

            result.add(target);
        }

        return result;
    }

    // ==================== 快捷方法 ====================

    /**
     * 专用于 HrmsOkrInfo 的树复制（默认字段）
     */
    public static List<HrmsOkrInfo> copyOkrTree(
            List<HrmsOkrInfo> sourceList) {
        return copyTree(sourceList, HrmsOkrInfo::new, "id", "parentId", "ancestors", "status");
    }

}
