package com.open.javabasetool.fileutils;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/10/12 21:56
 * @description 动态树形文件夹
 */
@Data
public class DynamicTreeFileNode {
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
    private List<DynamicTreeFileNode> sonFileList;
}
