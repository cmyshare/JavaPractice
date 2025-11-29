package com.open.objectdifftwo.model;

import com.open.objectdifftwo.DiffLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Charles94jp
 * @since 2023-10-16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanE extends BeanBase {
    @DiffLog(name = "名称")
    public String name;
}
