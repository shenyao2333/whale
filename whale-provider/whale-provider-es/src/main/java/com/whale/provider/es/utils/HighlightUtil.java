package com.whale.provider.es.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: sy
 * @Date: Created by 2021/8/12 16:26
 * @description:
 */
public class HighlightUtil {



    public static  <T>  ArrayList<T>  assignment(SearchHits<T> dataList){
        ArrayList<T> restList = new ArrayList<>();
        for (SearchHit<T> searchHit : dataList.getSearchHits()) {
            T content = searchHit.getContent();
            setVal(searchHit.getHighlightFields(),content);
            restList.add(content);
        }
        return   restList;
    }



    private  static <T> void setVal( Map<String, List<String>> highlightFields, T object) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field: fields) {
                field.setAccessible(true);
                if (field.getAnnotation(JsonProperty.class) != null) {
                    JsonProperty annotation = (field.getAnnotation(JsonProperty.class));
                    if (annotation != null) {
                        String jsonPropertyValue = annotation.value();
                        if (highlightFields.containsKey(jsonPropertyValue)){
                            field.set(object, highlightFields.get(jsonPropertyValue).get(0));
                        }
                    }
                }else{
                    String fieldName = field.getName();
                    if (highlightFields.containsKey(fieldName)){
                        field.set(object, highlightFields.get(fieldName).get(0));
                    }
                }
            }
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }




}
