package com.rubber.project.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author luffyu
 * Created on 2021/5/23
 */
@Component
public class ConfigUtils  implements InitializingBean {

    private static HotelDataConfig hotelData;



    @Autowired
    private HotelDataConfig hotelDataConfig;


    public static HotelDataConfig getHotelData(){
        return hotelData;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        hotelData = this.hotelDataConfig;
    }
}
