package com.open.javabasetool.objectdifftwo;

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
public class DiffWapper implements Serializable {
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
    private Difference diffValue;

    //public DiffWapper(String path, String logName, String op, Difference diffValue) {
    //    this.path = path;
    //    this.logName = logName;
    //    this.op = op;
    //    this.diffValue = diffValue;
    //}
    //
    //public String getPath() {
    //    return path;
    //}
    //
    //public void setPath(String path) {
    //    this.path = path;
    //}
    //
    //public String getLogName() {
    //    return logName;
    //}
    //
    //public void setLogName(String logName) {
    //    this.logName = logName;
    //}
    //
    //public String getOp() {
    //    return op;
    //}
    //
    //public void setOp(String op) {
    //    this.op = op;
    //}
    //
    //public Difference getDiffValue() {
    //    return diffValue;
    //}
    //
    //public void setDiffValue(Difference diffValue) {
    //    this.diffValue = diffValue;
    //}
}