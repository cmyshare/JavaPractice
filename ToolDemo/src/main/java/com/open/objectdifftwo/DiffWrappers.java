package com.open.objectdifftwo;

import com.open.javabasetool.objectdifftwo.BaseDifference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 对比条件输出类DiffWapper
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiffWrappers implements Serializable {
    private static final long serialVersionUID = -3232326683473741L;
    /**
     * 路径
     */
    private String path;
    /**
     * 日志名称
     */
    private String logName;
    /**
     * 操作类型
     */
    private String op = "";
    /**
     * 基础差别类Difference
     */
    private BaseDifference diffValue;
}