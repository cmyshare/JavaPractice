//package com.open.javabasetool.javadoc;
//
//import java.util.List;
//
///**
// * @version 1.0
// * @Author cmy
// * @Date 2024/1/19 14:17
// * @desc
// */
//public class DocVO {
//    private List<FieldVO> fieldVOList;
//
//    public DocVO(List<FieldVO> fieldVOList) {
//        this.fieldVOList = fieldVOList;
//    }
//
//    public static class FieldVO {
//        /**
//         * 属性名称
//         */
//        private String fieldName;
//
//        /**
//         * 属性类型
//         */
//        private String fieldType;
//
//        /**
//         * 属性注释
//         */
//        private String describe;
//
//        public FieldVO() {
//        }
//
//        public FieldVO(String fieldName, String fieldType, String describe) {
//            this.fieldName = fieldName;
//            this.fieldType = fieldType;
//            this.describe = describe;
//        }
//
//        public String getFieldName() {
//            return fieldName;
//        }
//
//        public void setFieldName(String fieldName) {
//            this.fieldName = fieldName;
//        }
//
//        public String getFieldType() {
//            return fieldType;
//        }
//
//        public void setFieldType(String fieldType) {
//            this.fieldType = fieldType;
//        }
//
//        public String getDescribe() {
//            return describe;
//        }
//
//        public void setDescribe(String describe) {
//            this.describe = describe;
//        }
//
//        @Override
//        public String toString() {
//            return "FieldVO{" +
//                    "fieldName='" + fieldName + '\'' +
//                    ", fieldType='" + fieldType + '\'' +
//                    ", describe='" + describe + '\'' +
//                    '}';
//        }
//    }
//
//    public List<FieldVO> getFieldVOList() {
//        return fieldVOList;
//    }
//
//    public void setFieldVOList(List<FieldVO> fieldVOList) {
//        this.fieldVOList = fieldVOList;
//    }
//}
