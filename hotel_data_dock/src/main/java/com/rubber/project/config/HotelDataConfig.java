package com.rubber.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 外部资源配置地址
 *
 * @author luffyu
 * Created on 2021/4/13
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rubber.hotel")
public class HotelDataConfig {

    /**
     * 是否开启任务
     */
    private boolean openTask;


    private int afterDaySize = 7;


    /**
     * 龙腾的接口url
     */
    private String ltUrl ;

    /**
     * 龙腾接口的相关参数
     */
    private String ltAppId;
    private String ltSecurityKey;
    private String ltUserName;
    private String ltPassWord;
    private String ltSignature;

}
