package com.open.objectdifftwo.model;

import com.open.javabasetool.objectdifftwo.DiffLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by colinsu
 *
 * @date 2023/9/19.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanD {

    @DiffLog(name = "nameD")
    private String nameD;
}
