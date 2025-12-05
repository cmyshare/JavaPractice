package com.open.objectdifftwo.model;

import com.open.objectdifftwo.DiffLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Charles94jp
 * @since 2023-10-16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BeanBase {
    /**
     * 实体编号（唯一标识）
     */
    @DiffLog(name = "id")
    protected Long id;

    protected Date createDate;

}
