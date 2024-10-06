package com.open.javabasetool.objectdifftwo;

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

    public static PlatType getById(Integer type) {
        for (PlatType e : values()) {
            if (e.getType().intValue() == type) {
                return e;
            }
        }
        return null;
    }

}
