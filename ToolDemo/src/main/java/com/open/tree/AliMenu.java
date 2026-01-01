package com.open.tree;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单对象
 * @date 2026/1/1
 */
@Data
public class AliMenu {
    /**
     * 菜单ID
     */
    private Long id;
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 是否选中
     */
    private Boolean choose = false; // 用于 filterAndHandler 测试
    /**
     * 祖级id路径（逗号分隔）
     * 例如：
     * 当前id=1，parentId=0，那么ancestors=”0”
     * 当前id=2，parentId=1，那么ancestors=”0,1”
     * 当前id=3，parentId=2，那么ancestors=”0,1,2”
     */
    private String ancestors;
    /**
     * 菜单层级深度 从1开始
     * 例如：
     * 当前id=1，parentId=0，那么depth=1
     * 当前id=2，parentId=1，那么depth=2
     * 当前id=3，parentId=2，那么depth=3
     */
    private Integer depth;
    /**
     * 子菜单列表
     */
    private List<AliMenu> subMenus = new ArrayList<>();

    /**
     * 构造函数
     * @param id 菜单ID
     * @param parentId 父菜单ID
     * @param name 菜单名称
     */
    public AliMenu(Long id, Long parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    /**
     * 构造函数
     * @param id 菜单ID
     * @param parentId 父菜单ID
     * @param name 菜单名称
     */
    public AliMenu(Long id, Long parentId, String ancestors, String name) {
        this.id = id;
        this.parentId = parentId;
        this.ancestors = ancestors;
        this.name = name;
    }
}
