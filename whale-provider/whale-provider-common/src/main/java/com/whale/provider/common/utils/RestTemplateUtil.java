package com.whale.provider.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 21:52
 * @description:
 */
@Slf4j
@Service
public class RestTemplateUtil {



    @Resource
    private RestTemplate restTemplate;



    /**
     * 封装的get请求，暂时只支持map传参，并且value只支持基本类型和String
     *
     * @param url
     * @param map
     * @return
     */
    public JSONObject doGet(String url, Map map) {
        url = getString(url, map);
        JSONObject forObject = restTemplate.getForObject(url, JSONObject.class);
        log.info("请求返回数据："+forObject);
        return forObject;
    }



    /**
     *
     * @param url 地址
     * @param headMap 请求报文头
     * @param map 请求头参数
     * @param body 请求体参数
     * @return
     */
    public  JSONObject doPost(String url,Map headMap ,Map map,JSONObject body) {
        url = getString(url, map);
        HttpHeaders headers = getHead();
        headers.setContentType(MediaType.APPLICATION_JSON);
        for (Object o : headMap.keySet()){
            headers.add(o.toString(),headMap.get(o).toString());
        }
        log.info("请求数据"+body);
        HttpEntity<String> httpEntity = new HttpEntity<>(body!=null?body.toString():null, headers);
        JSONObject forObject = restTemplate.postForEntity(url, httpEntity, JSONObject.class).getBody();
        log.info("请求返回数据："+forObject);
        return  forObject;

    }



    /**
     * 获取json的请求头
     * @return
     */
    private   HttpHeaders  getHead(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }





    private String getString(String url, Map map) {
        StringBuilder sb = new StringBuilder(url);
        if (map!=null&&map.size()>0){
            Iterator iterator = map.entrySet().iterator();
            if (sb.indexOf("?") < 0) {
                sb.append("?");
            } else {
                sb.append("&");
            }
            if (iterator.hasNext()) {
                Object element;
                while (iterator.hasNext()) {
                    element = iterator.next();
                    Map.Entry entry = (Map.Entry) element;
                    if (entry.getValue() != null) {
                        sb.append(element).append("&");
                    }
                }
                sb.delete(sb.length() - 1,sb.length() );
            }
        }
        log.info("请求总路径为："+sb);
        return sb.toString();
    }
}
