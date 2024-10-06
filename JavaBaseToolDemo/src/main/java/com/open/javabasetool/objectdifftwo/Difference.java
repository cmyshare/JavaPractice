package com.open.javabasetool.objectdifftwo;

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
public class Difference implements Serializable {
    private static final long serialVersionUID = 2321642126795290L;

    /**
     * 旧值
     */
    private Object oldValue;
    /**
     * 新值
     */
    private Object newValue;


    //public Difference(Object oldValue, Object newValue) {
    //    this.oldValue = oldValue;
    //    this.newValue = newValue;
    //}
    //
    //
    //public Object getOldValue() {
    //    return oldValue;
    //}
    //
    //public void setOldValue(Object oldValue) {
    //    this.oldValue = oldValue;
    //}
    //
    //public Object getNewValue() {
    //    return newValue;
    //}
    //
    //public void setNewValue(Object newValue) {
    //    this.newValue = newValue;
    //}
}
