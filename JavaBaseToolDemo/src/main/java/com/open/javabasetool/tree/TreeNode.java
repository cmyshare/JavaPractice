package com.open.javabasetool.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/8/30 15:01
 * @desc
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode implements Serializable {
    /**
     * 节点id
     */
    private String id;
    /**
     * 父节点id
     */
    private String parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 层级
     */
    private int level;
    /**
     * 排序
     */
    private int sort;
    /**
     * 子树
     */
    private List<TreeNode> childrenList;
    /**
     * 节点扩展字段键值对
     */
    private Map<String, Object> extendedFieldMap;
}
