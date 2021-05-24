package com.whale.provider.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 21:52
 * @description:
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RestTemplateUtil {

    private final RestTemplate restTemplate;


    /**
     *
     * @param url url
     * @param param 请求体参数
     * @return 响应json结果
     */
    public JSONObject  doPost(String url, JSONObject param) {
        // 设置请求头
        HttpHeaders headers = this.getHead();
        return postWithJson(url, headers, param, JSONObject.class);
    }

    /**
     *
     * @param responseType 响应泛型
     */
    public <T> T doPost(String url, JSONObject param, Class<T> responseType) {
        HttpHeaders headers = this.getHead();
        return postWithJson(url, headers, param, responseType);
    }


    public JSONObject  doPost(String url, Map<String,String> headers,  JSONObject param) {
        HttpHeaders httpHeaders = getHead();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        for (String key : headers.keySet()){
            httpHeaders.add(key,headers.get(key));
        }
        return postWithJson(url, httpHeaders, param, JSONObject.class);
    }


    public JSONObject  doPost(String url, Map<String,String> headers, Map<String,String> headersParams , JSONObject param) {
        url = this.completionUrl(url, headersParams);
        HttpHeaders httpHeaders = getHead();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        for (String key : headers.keySet()){
            httpHeaders.add(key,headers.get(key));
        }
        return postWithJson(url, httpHeaders, param, JSONObject.class);
    }



    /**
     * 获取json的请求头
     */
    private  HttpHeaders  getHead(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


    public  <T> T postWithJson(String url, HttpHeaders headers, JSONObject param, Class<T> responseType) {
        // 请求
        return postWithJson(url, headers, param==null?null:param.toString(), responseType);
    }


    public  <T> T postWithJson(String url, HttpHeaders headers, String param, Class<T> responseType) {
        // 请求中设置param和headers
        HttpEntity<String> request = new HttpEntity<>(param, headers);
        // post请求并返回
        log.info("rest----->请求路径为："+url);
        log.info("rest----->请求参数为："+param);
        long l1 = System.currentTimeMillis();
        T rest = restTemplate.postForObject(url, request, responseType);
        long l2 = System.currentTimeMillis();
        log.info("rest----->返回数据："+rest);
        log.info("rest----->花费时间："+ (l2-l1));
        return rest;
    }

    /**
     * get请求
     */
    public  JSONObject doGet(String url) {
        // get请求并返回
        log.info("rest----->GET请求，url为："+url);
        HttpHeaders head = this.getHead();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, head);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, JSONObject.class).getBody();
    }

    /**
     * get请求
     * @param url url
     * @param params 参数
     * @return json响应题
     */
    public  JSONObject doGet(String url,Map<String,String> params) {
        url =  this.completionUrl(url,params);
        HttpHeaders head = this.getHead();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, head);
        log.info("rest----->GET请求，url为："+url);
        return  restTemplate.exchange(url, HttpMethod.GET, requestEntity, JSONObject.class).getBody();

    }


    public JSONObject doGetWithHead(String url, HashMap<String, String> headers) {
        HttpHeaders head = this.getHead();
        if (headers!=null&&headers.size()>0){
            for (String key : headers.keySet()){
                head.add(key,headers.get(key));
            }
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(null, head);
        ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, JSONObject.class);
        return response.getBody();
    }

    public <T> T getForObject(String url, Class<T> responseType) {
        // get请求并返回
        return restTemplate.getForObject(url, responseType);
    }



    public String getForXml(String url, HttpHeaders headers) {
        headers.add("Accept", MediaType.APPLICATION_XML_VALUE);
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        log.info(url + "&&&&&" + response.toString() + "&&&&&" + response.getBody());
        return response.getBody();
    }


    /**
     * 带有文件
     * @param url
     */
    public void uploadByResource(String url) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new ClassPathResource("test.txt"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(url , HttpMethod.POST, requestEntity, JSONObject.class);
        JSONObject body = exchange.getBody();
    }


    private String completionUrl(String url, Map<String,String> map) {
        StringBuilder sb = new StringBuilder(url);
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            if (sb.indexOf("?") < 0) {
                sb.append("?");
            } else {
                sb.append("&");
            }
            if (iterator.hasNext()) {
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    if (entry.getValue() != null) {
                        sb.append(entry).append("&");
                    }
                }
                sb.delete(sb.length() - 1, sb.length());
            }
        }
        return sb.toString();
    }

}
