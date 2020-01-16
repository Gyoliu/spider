package com.genesis.x.robot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: liuxing
 * @Date: 2020/1/14 16:22
 * @Description:
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "weixin.robot")
public class WeixinRobotConfig {

    private String appId;
    private String token;
    private String encodingAESKey;
    private String messageApi;
    private String lexicalAnalysisApi;
    private String sensitiveApi;
    private String casualChatApi;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingAESKey() {
        return encodingAESKey;
    }

    public void setEncodingAESKey(String encodingAESKey) {
        this.encodingAESKey = encodingAESKey;
    }

    public String getMessageApi() {
        return messageApi;
    }

    public void setMessageApi(String messageApi) {
        this.messageApi = messageApi;
    }

    public String getLexicalAnalysisApi() {
        return lexicalAnalysisApi;
    }

    public void setLexicalAnalysisApi(String lexicalAnalysisApi) {
        this.lexicalAnalysisApi = lexicalAnalysisApi;
    }

    public String getSensitiveApi() {
        return sensitiveApi;
    }

    public void setSensitiveApi(String sensitiveApi) {
        this.sensitiveApi = sensitiveApi;
    }

    public String getCasualChatApi() {
        return casualChatApi;
    }

    public void setCasualChatApi(String casualChatApi) {
        this.casualChatApi = casualChatApi;
    }
}