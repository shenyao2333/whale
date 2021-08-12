package com.whale.provider.basices.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @Author: sy
 * @Date: Created by 2021/8/12 16:41
 * @description:
 */
public class ReflectUtil {

    /**
     * 根据属性名给对象赋值
     * @param key 属性名
     * @param value 属性值
     * @param object 对象
     * @param <T>
     * @return
     */
    public static  <T> T setVal(String key, Object value, T object) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field: fields) {
                field.setAccessible(true);
                if (field.getAnnotation(JsonProperty.class) != null) {
                    JsonProperty annotation = (field.getAnnotation(JsonProperty.class));
                    if (annotation != null) {
                        String jsonPropertyValue = annotation.value();
                        if (key.equals(jsonPropertyValue)) {
                            field.set(object, value);
                            break;
                        }
                    }
                }else{
                    String fieldName = field.getName();
                    if(key.equals(fieldName)){
                        field.set(object,value);
                        break;
                    }
                }
            }
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return object;
    }


    /**
     * 通过属性名获取属性的值
     *
     * @param key   属性名
     * @param object 对象
     */
    public static <T> Object getValue(String key, T object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(JsonProperty.class) != null) {
                JsonProperty annotation = (field.getAnnotation(JsonProperty.class));
                if (annotation != null) {
                    String jsonPropertyValue = annotation.value();
                    if (key.equals(jsonPropertyValue)) {
                        Object values = null;
                        try {
                            values = field.get(object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        return values;
                    }
                }
            }else{
                //获取的不是@jsonproperty的属性名称
                String fieldName = field.getName();
                if (key.equals(fieldName)) {
                    Object values = null;
                    try {
                        values = field.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return values;
                }
            }
        }
        return null;
    }



    /**
     * 根据属性名给对象赋值
     * @param object 对象
     * @param <T>
     * @return
     */
    public static  <T> T setVal(Map<String,Object> map, T object) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field: fields) {
                field.setAccessible(true);
                if (field.getAnnotation(JsonProperty.class) != null) {
                    JsonProperty annotation = (field.getAnnotation(JsonProperty.class));
                    if (annotation != null) {
                        String jsonPropertyValue = annotation.value();
                        if (map.containsKey(jsonPropertyValue)) {
                            field.set(object, map.get(jsonPropertyValue));
                        }
                    }
                }else{
                    String fieldName = field.getName();
                    if (map.containsKey(fieldName)) {
                        field.set(object, map.get(fieldName));
                    }
                }
            }
            Class superclass = object.getClass();
            while (superclass != null) {
                superclass = superclass.getSuperclass();
                if (superclass.getName().equals("java.lang.Object")) {
                    break;
                }
                //获取父类的属性
                Field[] superField = superclass.getDeclaredFields();
                for (Field field: superField) {
                    field.setAccessible(true);
                    if (field.getAnnotation(JsonProperty.class) != null) {
                        JsonProperty annotation = (field.getAnnotation(JsonProperty.class));
                        if (annotation != null) {
                            String jsonPropertyValue = annotation.value();
                            if (map.containsKey(jsonPropertyValue)) {
                                field.set(object, map.get(jsonPropertyValue));
                            }
                        }
                    }else{
                        String fieldName = field.getName();
                        if (map.containsKey(fieldName)) {
                            field.set(object, map.get(fieldName));
                        }
                    }
                }
            }
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return object;
    }

}
