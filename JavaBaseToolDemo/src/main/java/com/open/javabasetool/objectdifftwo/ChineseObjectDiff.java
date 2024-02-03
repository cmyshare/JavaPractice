package com.open.javabasetool.objectdifftwo;

import java.util.List;

/**
 * 对象对比中文输出类ChineseObjectDiff
 */
public class ChineseObjectDiff extends AbstractObjectDiff {
    @Override
    protected String genDiffStr(Object sourceObject, Object targetObject) throws Exception {
        List<DiffWapper> diffWappers = generateDiff(sourceObject, targetObject);
        return DiffUtils.genDiffStr(diffWappers);
    }

}
