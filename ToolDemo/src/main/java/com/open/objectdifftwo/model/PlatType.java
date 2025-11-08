package com.open.objectdifftwo.model;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @desc 平台枚举
 */
@Getter
@AllArgsConstructor
public enum PlatType {

    TB(1, "淘宝"),
    TM(2, "天猫");

    private final Integer type;
    private final String name;


    /**
     * 根据type获取name，所有枚举必须用用这个公共方法
     * @param type
     * @return
     */
    public static String getNameByType(Integer type) {
        if (ObjectUtil.isEmpty(type)) {
            return null;
        }
        for (PlatType e : values()) {
            if (e.getType().intValue() == type) {
                return e.getName();
            }
        }
        return null;
    }

}
