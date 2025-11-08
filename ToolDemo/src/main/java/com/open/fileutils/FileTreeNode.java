package com.open.fileutils;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/10/12 21:56
 * @description 文件夹树节点
 */
@Data
public class FileTreeNode {
    /**
     * 父级文件夹名称
     */
    private String parentFileName;
    /**
     * 当前文件夹名称
     */
    private String fileName;
    /**
     * 当前文件夹中文件数据：name+url键值对
     */
    private Map<String,String> nameUrlMap;
    /**
     * 子级文件夹
     */
    private List<FileTreeNode> sonFileList;
}
