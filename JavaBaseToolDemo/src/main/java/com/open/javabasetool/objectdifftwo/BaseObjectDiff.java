package com.open.javabasetool.objectdifftwo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 对象对比基础类AbstractObjectDiff
 */
public abstract class BaseObjectDiff {

    /**
     * 空字段数组
     */
    public static final Field[] EMPTY_FIELD_ARRAY = {};

    protected abstract String genDiffStr(Object sourceObject, Object targetObject) throws Exception;

    /**
     * 生成中文Diff
     *
     * @param sourceObject
     * @param targetObject
     * @return
     * @throws Exception
     */
    public static String genChineseDiffStr(Object sourceObject, Object targetObject) throws Exception {
        List<DiffWrappers> diffWrappers = generateDiff(sourceObject, targetObject);
        return DiffUtils.genDiffStr(diffWrappers);
    }

    /**
     * 生成Diff
     *
     * @param sourceObject
     * @param targetObject
     * @return
     * @throws Exception
     */
    public static List<DiffWrappers> generateDiff(Object sourceObject, Object targetObject) throws Exception {
        return generateDiff("", "", sourceObject, targetObject);
    }

    /**
     * 生成Diff
     *
     * @param path
     * @param cnName
     * @param sourceObject
     * @param targetObject
     * @return
     * @throws Exception
     */
    private static List<DiffWrappers> generateDiff(String path, String cnName, Object sourceObject, Object targetObject)
            throws Exception {
        List<DiffWrappers> diffWrappersList = new ArrayList<>();
        //判断对比数据空值
        if (sourceObject == null && targetObject == null) {
            return null;
        }

        if (sourceObject == null || targetObject == null) {
            DiffWrappers diffWrappers = DiffUtils
                    .getDiffWrappers(path, cnName, (sourceObject == null ? null : getObjectString(sourceObject)),
                            targetObject == null ? null : getObjectString(targetObject));
            diffWrappersList.add(diffWrappers);
            return diffWrappersList;
        }

        //先判断object类型
        if (!sourceObject.getClass().getName().equals(targetObject.getClass().getName())) {
            return null;
        }
        //判断hash地址
        if (sourceObject.hashCode() == targetObject.hashCode()) {
            return null;
        }

        Field[] fields = getAllFields(sourceObject.getClass());

        for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];
            Class<?> type = field.getType();
            String newPath = path + "/" + field.getName();
            String nameCn = newPath;
            field.setAccessible(true);
            //是否存在注释
            if (field.isAnnotationPresent(DiffLog.class)) {
                DiffLog logVo = field.getAnnotation(DiffLog.class);
                if (cnName == null || cnName.equals("")) {
                    nameCn = logVo.name();
                } else {
                    nameCn = cnName + "." + logVo.name();
                }
                if (logVo.ignore()) {
                    continue;
                }
            } else {
                continue;
            }
            if (Collection.class.isAssignableFrom(type)) {
                //先判断一下集合
                List<?> oldList = (List) field.get(sourceObject);
                List<?> newList = (List) field.get(targetObject);
                Map<Object, Object> oldFilterMap = new HashMap<>();
                Map<Object, Object> newFilterMap = new HashMap<>();
                Class<?> genricClass = null;
                if (field.getGenericType() instanceof ParameterizedType) {
                    // 获取泛型 Class
                    genricClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                }
                if (genricClass == null) {
                    continue;
                }
                Field[] collFields = getAllFields(genricClass);
                Field keyField = null;
                String keyCnName = "";
                for (int j = 0; j < collFields.length; j++) {
                    if (collFields[j].isAnnotationPresent(DiffLogKey.class)) {
                        keyField = collFields[j];
                        DiffLogKey keyFieldAnnotation = keyField.getAnnotation(DiffLogKey.class);
                        keyCnName = keyFieldAnnotation.name();
                        break;
                    }
                }
                if (keyField == null) {
                    continue;
                }
                keyField.setAccessible(true);
                if (newList != null) {
                    for (Object o : newList) {
                        Object o1 = keyField.get(o);
                        newFilterMap.put(o1, o);
                    }
                }
                if (oldList != null) {
                    for (Object old : oldList) {
                        Object o2 = keyField.get(old);
                        oldFilterMap.put(o2, old);
                    }
                }
                Set<Object> oldKeySets = oldFilterMap.keySet();
                Set<Object> newKeySets = newFilterMap.keySet();
                Set<Object> resultSet = new HashSet<>();
                resultSet.clear();
                resultSet.addAll(oldKeySets);
                resultSet.addAll(newKeySets);
                //取两个之间的并集,然后统一输出
                for (Object result : resultSet) {
                    Object oldOb = oldFilterMap.get(result);
                    Object newOb = newFilterMap.get(result);

                    //删除和新增，不输出嵌套集合nestedCollect=false
                    if (ObjectUtil.isEmpty(oldOb) || ObjectUtil.isEmpty(newOb)){
                        //删除
                        if (ObjectUtil.isNotEmpty(oldOb)){
                            Class<?> clazz1 = oldOb.getClass();
                            Field[] fields1 = clazz1.getDeclaredFields();
                            for (Field field1 : fields1) {
                                field1.setAccessible(true);
                                if (field1.isAnnotationPresent(DiffLog.class)) {
                                    DiffLog annotation = field1.getAnnotation(DiffLog.class);
                                    if (!annotation.nestedCollect()) {
                                        List<Object> fieldValue = ObjectUtil.isNotEmpty(field1.get(oldOb))?(List<Object>) field1.get(oldOb):new ArrayList<>();
                                        List<String> integerList = new ArrayList<>();
                                        integerList.add(fieldValue.size()+"条数据");
                                        field1.set(oldOb, integerList);
                                        //方案二，根据反射循环这个列表，把每行数据加注解的字段，逐个读取，拼接每行数据
                                    }
                                }
                            }
                        }
                        //新增
                        if (ObjectUtil.isNotEmpty(newOb)){
                            Class<?> clazz2 = newOb.getClass();
                            Field[] fields2 = clazz2.getDeclaredFields();
                            for (Field field2 : fields2) {
                                field2.setAccessible(true);
                                if (field2.isAnnotationPresent(DiffLog.class)) {
                                    DiffLog annotation = field2.getAnnotation(DiffLog.class);
                                    if (!annotation.nestedCollect()) {
                                        List<Object> fieldValue = ObjectUtil.isNotEmpty(field2.get(newOb))?(List<Object>) field2.get(newOb):new ArrayList<>();
                                        List<String> integerList = new ArrayList<>();
                                        integerList.add(fieldValue.size()+"条数据");
                                        field2.set(newOb, integerList);
                                        //方案二，根据反射循环这个列表，把每行数据加注解的字段，逐个读取，拼接每行数据
                                    }
                                }
                            }
                        }
                    }

                    String oBPath = newPath + "/" + (result == null ? "null" : result.toString());
                    String oBcnName = nameCn + "." + keyCnName + "[" + (result == null ? "null" : result.toString()) + "]";
                    List<DiffWrappers> collectDiff = generateDiff(oBPath, oBcnName, oldOb, newOb);
                    if (collectDiff != null) {
                        diffWrappersList.addAll(collectDiff);
                    }
                }
            }
            else {
                //判断是否java内部类
                if (isJavaClass(type)) {
                    DiffWrappers diffWrappers = generateOneDiffs(newPath, nameCn, field, sourceObject, targetObject);
                    if (diffWrappers != null) {
                        diffWrappersList.add(diffWrappers);
                    }
                } else {
                    //如自定义bean则走递归方法
                    List<DiffWrappers> collectDiff = generateDiff(newPath, nameCn,
                            field.get(sourceObject), field.get(targetObject));
                    if (collectDiff != null) {
                        diffWrappersList.addAll(collectDiff);
                    }
                }
            }
        }

        return diffWrappersList;
    }


    /**
     * 判断Java类生成一个Diff
     *
     * @param path
     * @param nameCn
     * @param field
     * @param source
     * @param target
     * @return
     * @throws Exception
     */
    private static DiffWrappers generateOneDiffs(String path, String nameCn, Field field, Object source, Object target)
            throws Exception {
        //判断是普通Object还是Collection
        //过滤一些不需要的key
        //Collection需要根据某个key进行排序,然后比较
        DiffUtils diffUtils = new DiffUtils();
        String typeName = field.getType().getName();
        Class<?> type = field.getType();
        field.setAccessible(true);
        DiffLog logVo = field.getAnnotation(DiffLog.class);
        String dateFormat = "";
        if (logVo != null) {
            dateFormat = logVo.dateFormat();
            if (logVo.ignore()) {
                return null;
            }
        }
        Class dictEnum = null;
        if (logVo != null) {
            dictEnum = logVo.dictEnum();
            if (logVo.ignore()) {
                return null;
            }
        }
        if ("java.lang.String".equals(typeName)) {
            String oldStr = (String) field.get(source);
            String newStr = (String) field.get(target);
            return diffUtils.get(path, nameCn, oldStr, newStr);
        }
        else if ("java.sql.Timestamp".equals(typeName)) {
            DateFormat format =
                    new SimpleDateFormat(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd HH:mm:ss" : dateFormat);
            java.sql.Timestamp newTime = (java.sql.Timestamp) field.get(target);
            java.sql.Timestamp oldTime = (java.sql.Timestamp) field.get(source);
            String newTempTimeStr = "";
            String oldTimeTimeStr = "";
            if (newTime != null) {
                newTempTimeStr = format.format(newTime);
            }
            if (oldTime != null) {
                oldTimeTimeStr = format.format(oldTime);
            }
            if (oldTime == newTime && oldTime == null) {
                return null;
            }
            if (oldTime == null || newTime == null) {
                return DiffUtils.getDiffWrappers(path, nameCn, oldTime == null ? null : oldTimeTimeStr, newTime == null ? null : newTempTimeStr);
            }

            if (!StringUtils.equals(newTempTimeStr, oldTimeTimeStr)) {
                return DiffUtils.getDiffWrappers(path, nameCn, format.format(oldTime), format.format(newTime));
            }
        }
        else if ("java.lang.Long".equals(typeName) || Long.TYPE == type) {
            Long oldValue = (Long) field.get(source);
            Long newValue = (Long) field.get(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);

        }
        else if ("java.lang.Integer".equals(typeName) || Integer.TYPE == type) {
            //旧方案
            //Integer oldValue =(Integer) field.get(source);
            //Integer newValue = (Integer) field.get(target);
            //return diffUtils.get(path, nameCn, oldValue, newValue);

            //新方案，增加注解参数放入Class枚举类：dictEnum =PlatType.class
            Integer oldValue = (Integer) field.get(source);
            Integer newValue = (Integer) field.get(target);
            if (dictEnum != null && dictEnum != Integer.class) {
                // 获取枚举类的所有常量
                for (Object enumConstant : dictEnum.getEnumConstants()) {
                    Method getNameByType = dictEnum.getDeclaredMethod("getNameByType", Integer.class);
                    if (ObjectUtil.isNotEmpty(getNameByType)) {
                        String oldName = (String) getNameByType.invoke(enumConstant, oldValue);
                        String newName = (String) getNameByType.invoke(enumConstant, newValue);
                        // 如果找到了匹配的值，返回差异
                        if (oldName != null && newName != null) {
                            return diffUtils.get(path, nameCn, oldName, newName);
                        }
                    }
                }
            }
            // 如果没有找到匹配的值，或者 dictEnum 为空，直接返回旧值和新值
            return diffUtils.get(path, nameCn, oldValue, newValue);

        }
        else if ("java.lang.Boolean".equals(typeName) || Boolean.TYPE == type) {
            Boolean oldValue = (Boolean) field.get(source);
            Boolean newValue = (Boolean) field.get(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);

        }
        else if ("java.math.BigDecimal".equals(typeName)) {
            BigDecimal oldValue = (BigDecimal) field.get(source);
            BigDecimal newValue = (BigDecimal) field.get(target);
            if (oldValue != null && newValue != null && oldValue.compareTo(newValue) == 0) {
                newValue = oldValue;
            }
            return diffUtils.get(path, nameCn, oldValue, newValue);
        }
        else if ("java.lang.Byte".equals(typeName) || Byte.TYPE == type) {
            //预留不处理
            Byte oldValue = (Byte) field.get(source);
            Byte newValue = (Byte) field.get(target);
            if (oldValue != null && newValue != null && oldValue.compareTo(newValue) == 0) {
                newValue = oldValue;
            }
            return diffUtils.get(path, nameCn, oldValue, newValue);
        }
        else if ("java.lang.Short".equals(typeName) || Short.TYPE == type) {
            Short oldValue = (Short) field.get(source);
            Short newValue = (Short) field.get(target);
            if (oldValue != null && newValue != null && oldValue.compareTo(newValue) == 0) {
                newValue = oldValue;
            }
            return diffUtils.get(path, nameCn, oldValue, newValue);
            //预留不处理 有需要在处理
        }
        else if ("java.lang.Float".equals(typeName) || Float.TYPE == type) {
            Float oldValue = field.getFloat(source);
            Float newValue = field.getFloat(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);

        }
        else if ("java.lang.Double".equals(typeName) || Double.TYPE == type) {
            String oldValue = field.get(source) == null ? null : String.valueOf(field.get(source));
            String newValue = field.get(target) == null ? null : String.valueOf(field.get(target));
            return diffUtils.get(path, nameCn, oldValue, newValue);

        }
        else if ("java.util.Date".equals(typeName)) {
            DateFormat format = new SimpleDateFormat(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd" : dateFormat);
            Date newTime = (Date) field.get(target);
            Date oldTime = (Date) field.get(source);
            String newTempTimeStr = "";
            String oldTimeTimeStr = "";
            if (newTime != null) {
                newTempTimeStr = format.format(newTime);
            }
            if (oldTime != null) {
                oldTimeTimeStr = format.format(oldTime);
            }
            if (oldTime == newTime && oldTime == null) {
                return null;
            }
            if (oldTime == null || newTime == null) {
                return DiffUtils.getDiffWrappers(path, nameCn, oldTime == null ? null : oldTimeTimeStr, newTime == null ? null : newTempTimeStr);
            }
            if (!StringUtils.equals(newTempTimeStr, oldTimeTimeStr)) {
                return DiffUtils.getDiffWrappers(path, nameCn, oldTimeTimeStr, newTempTimeStr);
            }
        }
        else if ("java.time.LocalDateTime".equals(typeName)) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd hh:mm:ss" : dateFormat);
            java.time.LocalDateTime newTime = (java.time.LocalDateTime) field.get(target);
            java.time.LocalDateTime oldTime = (java.time.LocalDateTime) field.get(source);
            String newTempTimeStr = "";
            String oldTimeTimeStr = "";
            if (newTime != null) {
                newTempTimeStr = format.format(newTime);
            }
            if (oldTime != null) {
                oldTimeTimeStr = format.format(oldTime);
            }
            if (oldTime == newTime && oldTime == null) {
                return null;
            }
            if (oldTime == null || newTime == null) {
                return DiffUtils.getDiffWrappers(path, nameCn, oldTime == null ? null : oldTimeTimeStr, newTime == null ? null : newTempTimeStr);
            }
            if (!StringUtils.equals(newTempTimeStr, oldTimeTimeStr)) {
                return DiffUtils.getDiffWrappers(path, nameCn, oldTimeTimeStr, newTempTimeStr);
            }
        }

        return null;
    }


    /**
     * 获取对象字符串
     *
     * @param source
     * @return
     * @throws Exception
     */
    private static String getObjectString(Object source) throws Exception {
        if (source == null) {
            return "";
        }
        List<String> logList = new ArrayList<>();
        Field[] fields = getAllFields(source.getClass());
        for (int i = 0; i < fields.length; i++) {
            String logStr = "";
            Field field = fields[i];
            String typeName = field.getType().getName();
            field.setAccessible(true);
            DiffLog logVo = field.getAnnotation(DiffLog.class);
            String nameCn = field.getName();
            String dateFormat = "";
            if (logVo != null) {
                nameCn = logVo.name();
                dateFormat = logVo.dateFormat();
            } else {
                continue;
            }
            if ("java.lang.String".equals(typeName)) {
                String oldStr = (String) field.get(source);
                logStr = "[" + nameCn + "]=" + oldStr + " ";
                logList.add(logStr);
            } else if ("java.util.Date".equals(typeName)) {
                DateFormat format =
                        new SimpleDateFormat(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd HH:mm:ss" : dateFormat);
                Date oldTime = (Date) field.get(source);
                if (oldTime != null) {
                    logStr = "[" + nameCn + "]=" + format.format(oldTime) + " ";
                    logList.add(logStr);
                }
            } else {
                Object oldValue = field.get(source);
                logStr = "[" + nameCn + "]=" + (oldValue == null ? "null" : oldValue.toString()) + " ";
                logList.add(logStr);
            }


        }
        return StringUtils.join(logList.iterator(), ",").trim();

    }


    /**
     * 判断Java类
     *
     * @param clz
     * @return
     */
    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }

    /**
     * 获取所有字段
     * org.apache.commons.lang3.reflect.FieldUtils.getAllFields(),version 3.13.0
     */
    private static Field[] getAllFields(final Class<?> cls) {
        if (cls == null)
            throw new NullPointerException("cls");
        final List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields.toArray(EMPTY_FIELD_ARRAY);
    }

}
