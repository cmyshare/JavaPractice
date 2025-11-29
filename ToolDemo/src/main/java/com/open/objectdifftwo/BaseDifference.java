package com.open.objectdifftwo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 基础差别类Difference
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDifference implements Serializable {
    private static final long serialVersionUID = 2321642126795290L;

    /**
     * 旧值
     */
    private Object oldValue;
    /**
     * 新值
     */
    private Object newValue;

}
