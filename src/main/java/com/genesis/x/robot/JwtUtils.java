package com.genesis.x.robot;


import com.genesis.x.service.SpringContextHolder;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * @Author: liuxing
 * @Date: 2020/1/14 10:35
 * @Description:
 */
public class JwtUtils {

    /**
     * 加密方式
     */
    private static final HashMap<String, Object> HEADERS = new HashMap<String, Object>(){
        {
            put("typ","JWT");
            put("alg",SignatureAlgorithm.HS256.getValue());
        }
    };

    public static String encode(String secret, String query){
        Assert.hasText(secret, "JwtUtils encode method parameter secret is not empty!");
        Assert.hasText(query, "JwtUtils encode method parameter query is not empty!");
        JwtBuilder jwtBuilder = Jwts.builder().setHeader(HEADERS).setPayload(query);
        try {
            byte[] bytes = secret.getBytes("UTF-8");
            jwtBuilder.signWith(SignatureAlgorithm.HS256, bytes);
        } catch (UnsupportedEncodingException e) {
            throw new JwtException("JwtUtils encode method secret.getBytes error!", e);
        }
        return jwtBuilder.compact();
    }

    public static String encode(String query){
        WeixinRobotConfig bean = SpringContextHolder.getBean(WeixinRobotConfig.class);
        String encodingAESKey = bean.getEncodingAESKey();
        return encode(encodingAESKey, query);
    }

    /*public static void main(String[] args) throws UnsupportedEncodingException {
        String s = Jwts.builder()
                //.setSubject("{\"username\":\"username\",\"msg\":\"你好\"}")
                //.setId("72b6734a-2df2-425e-b6d5-5fbd5b3c1ea9")
                //.setIssuedAt(Date.from(Instant.ofEpochSecond(1578970616)))
                //.setExpiration(Date.from(Instant.ofEpochSecond(1578974216)))
                //.claim("name", "John Doe")
                //.claim("admin", true)
                //.setHeader(header)
                .setPayload("{\"username\":\"A\",\"msg\":\"跨法人费用报销\"}")
                .signWith(SignatureAlgorithm.HS256, "9vm8dwEpcoImVk4u2ZFEzoTg4sxv8Hbezsgnr3iZFEO".getBytes("UTF-8"))
                .compact();
        System.out.println(s);
    }*/

}