package com.genesis.x.robot;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liuxing
 * @Date: 2020/1/15 14:30
 * @Description:
 */
@Component
public class WeixinRobotApi {

    private static final Logger log = LoggerFactory.getLogger(WeixinRobotApi.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeixinRobotConfig weixinRobotConfig;

    public WeixinRobotMessageDto message(String username, String msg){
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("msg", msg);
        String messageApiUrl = weixinRobotConfig.getMessageApi();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String encode = JwtUtils.encode(weixinRobotConfig.getEncodingAESKey(), JSON.toJSONString(data));
        HttpEntity<String> entity = new HttpEntity<>("{\"query\":\"" + encode + "\"}", headers);
        ResponseEntity<WeixinRobotMessageDto> exchange = restTemplate.exchange(messageApiUrl, HttpMethod.POST, entity, WeixinRobotMessageDto.class);
        if(exchange.getStatusCode().is2xxSuccessful() && exchange.getBody() != null){
            return exchange.getBody();
        } else {
            return null;
        }
    }

    public Map<String, Object> chat(String username, String msg){
        HashMap<String, Object> data = new HashMap<>(2);
        data.put("uid", username);
        data.put("data", new HashMap<String, Object>(1){
            {
                put("q", msg);
            }
        });
        String messageApiUrl = weixinRobotConfig.getCasualChatApi();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String encode = JwtUtils.encode(weixinRobotConfig.getEncodingAESKey(), JSON.toJSONString(data));
        HttpEntity<String> entity = new HttpEntity<>("{\"query\":\"" + encode + "\"}", headers);
        ResponseEntity<String> exchange = restTemplate.exchange(messageApiUrl, HttpMethod.POST, entity, String.class);
        if(exchange.getStatusCode().is2xxSuccessful() && exchange.getBody() != null){
            Map map = JSON.parseObject(exchange.getBody(), Map.class);
            return map;
        } else {
            return null;
        }
    }

    /**
     * 是否涉及辱骂、政治、黄色
     * @param username
     * @param msg
     * @return
     */
    public boolean sensitive(String username, String msg){
        HashMap<String, Object> data = new HashMap<>(2);
        data.put("uid", username);
        data.put("data", new HashMap<String, Object>(2){
            {
                put("q", msg);
                // "cnn"（默认） 或 "bert" （效果更好但是速度较慢）
                put("model", "cnnn");
            }
        });
        String messageApiUrl = weixinRobotConfig.getCasualChatApi();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String encode = JwtUtils.encode(weixinRobotConfig.getEncodingAESKey(), JSON.toJSONString(data));
        HttpEntity<String> entity = new HttpEntity<>("{\"query\":\"" + encode + "\"}", headers);
        ResponseEntity<String> exchange = restTemplate.exchange(messageApiUrl, HttpMethod.POST, entity, String.class);
        if(exchange.getStatusCode().is2xxSuccessful() && exchange.getBody() != null){
            Map map = JSON.parseObject(exchange.getBody(), Map.class);
            String o = map.get("dirty_politics") == null ? "0.0" : map.get("dirty_politics").toString();
            String o1 = map.get("dirty_porno") == null ? "0.0" : map.get("dirty_porno").toString();
            String o2 = map.get("dirty_curse") == null ? "0.0" : map.get("dirty_curse").toString();
            if(Float.parseFloat(o) > 0 || Float.parseFloat(o1) > 0 || Float.parseFloat(o2) > 0){
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

}