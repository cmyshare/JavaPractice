package com.open.objectdifftwo;

import java.util.List;

/**
 * 对比工具类
 *
 * @param <T>
 */
public class DiffUtils<T> {
    /**
     * 操作类型修改
     */
    public final static String CHANGE = "CHANGE";
    /**
     * 操作类型删除
     */
    public final static String REMOVE = "REMOVE";
    /**
     * 操作类型新增
     */
    public final static String ADD = "ADD";

    public DiffWrappers get(String path, String nameCn, T oldValue, T newValue) {
        if (oldValue == newValue && oldValue == null) {
            return null;
        }
        if (oldValue == null || newValue == null) {
            return getDiffWrappers(path, nameCn, oldValue, newValue);
        }
        if (!newValue.equals(oldValue)) {
            return getDiffWrappers(path, nameCn, oldValue, newValue);
        }
        return null;
    }


    public static DiffWrappers getDiffWrappers(String path, String nameCn, Object oldStr, Object newStr) {
        String op = CHANGE;
        if (newStr == null && oldStr != null) {
            op = REMOVE;
        }
        if (oldStr == null && newStr != null) {
            op = ADD;
        }
        return new DiffWrappers(path, nameCn, op, new BaseDifference(oldStr, newStr));
    }


    public static String genDiffStr(List<DiffWrappers> diffWrappersList) {
        StringBuffer sb = new StringBuffer();

        if (diffWrappersList != null && diffWrappersList.size() > 0) {
            for (DiffWrappers diffWrappers : diffWrappersList) {
                String op = diffWrappers.getOp();
                String opCn = "修改为";
                if (op.equals(ADD)) {
                    opCn = "添加";
                    sb.append(String.format("在「%s」下 %s[%s]", diffWrappers.getLogName(), opCn, diffWrappers.getDiffValue().getNewValue()));
                } else if (op.equals(REMOVE)) {
                    opCn = "删除";
                    sb.append(String.format("在「%s」下 %s[%s]", diffWrappers.getLogName(), opCn, diffWrappers.getDiffValue().getOldValue()));
                } else {
                    sb.append(String.format("「%s」由 [%s]%s[%s]", diffWrappers.getLogName(), diffWrappers.getDiffValue().getOldValue(), opCn, diffWrappers.getDiffValue().getNewValue()));
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }


}
