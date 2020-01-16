package com.genesis.x.robot;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.StopWatch;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @Author: liuxing
 * @Date: 2020/1/14 16:55
 * @Description:
 */
@Slf4j
@Configuration
public class HttpClientUtils {

    @Bean
    public RestTemplate restTemplate(){
        return create();
    }

    public static RestTemplate create(){
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(60, TimeUnit.SECONDS);
        poolingHttpClientConnectionManager.setMaxTotal(100);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(100);

        RequestConfig requestConfig = RequestConfig.custom()
                // 指从连接池获取连接的timeout
                .setConnectionRequestTimeout(10000)
                // 指客户端和服务器建立连接的timeout
                .setConnectTimeout(10000)
                // 指客户端从服务器读取数据的timeout
                .setSocketTimeout(60000)
                .build();
        // HttpClients
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                // 失败重试
                .setRetryHandler(new StandardHttpRequestRetryHandler(0, false))
                .build();

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setInterceptors(new ArrayList<ClientHttpRequestInterceptor>(){
            {
                add(new ClientHttpRequestInterceptor() {
                    @Override
                    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
                        StopWatch watch = new StopWatch("restTemplateIntercept");
                        watch.start();
                        ClientHttpResponse response = null;
                        Exception ex1 = null;
                        try {
                            response = clientHttpRequestExecution.execute(httpRequest, bytes);
                        } catch (Exception ex){
                            ex1 = ex;
                        } finally {
                            watch.stop();
                            if(ex1 == null){
                                log.info("[调用时间:{}毫秒] - [调用{}:{}] - [参数:{}]", watch.getTotalTimeMillis(), httpRequest.getMethod().name(), httpRequest.getURI(), new String(bytes, "UTF-8"));
                            } else {
                                log.error("[调用时间:{}毫秒] - [调用{}:{}] - [参数:{}] - [异常：]", watch.getTotalTimeMillis(), httpRequest.getMethod().name(), httpRequest.getURI(), new String(bytes, "UTF-8"), ex1);
                            }
                        }
                        return response;
                    }
                });
            }
        });
        return restTemplate;

    }

}