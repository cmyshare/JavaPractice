package com.open.tree;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CmyShare
 * @date 2026/1/1
 */
@Data
@NoArgsConstructor
public class HrmsOkrInfo {
    private String id;
    private String parentId;
    private String ancestors;
    private Integer status;
    private String title;

    // 构造器
    public HrmsOkrInfo(String id, String parentId, String ancestors, Integer status, String title) {
        this.id = id;
        this.parentId = parentId;
        this.ancestors = ancestors;
        this.status = status;
        this.title = title;
    }

    // Getter / Setter（省略，实际需补全）
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    public String getAncestors() { return ancestors; }
    public void setAncestors(String ancestors) { this.ancestors = ancestors; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @Override
    public String toString() {
        return String.format("HrmsOkrInfo{id='%s', parentId='%s', ancestors='%s', status=%d, title='%s'}",
                id, parentId, ancestors, status, title);
    }
}
