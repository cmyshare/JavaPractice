package com.open.javabasetool.objectdifftwo;

import java.util.List;

/**
 * 对象对比中文输出类ChineseObjectDiff
 */
public class ChineseObjectDiff extends BaseObjectDiff {
    @Override
    protected String genDiffStr(Object sourceObject, Object targetObject) throws Exception {
        List<DiffWrappers> diffWrappers = generateDiff(sourceObject, targetObject);
        return DiffUtils.genDiffStr(diffWrappers);
    }
}
