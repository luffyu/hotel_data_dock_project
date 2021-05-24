package com.rubber.project.handler.impl.origin.lt.request;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
@Data
public class LtHotelRequest extends LtBaseRequest {


    /**
     * 国家/地区 ID
     */
    private String countryId = "0001";

    /**
     * 省份 ID
     */
    private String provinceId;

    /**
     * 城市Id
     */
    private String cityId;

    /**
     * 酒店Id
     */
    private String hotelId;


    /**
     * 入住的开始结束时间
     */
    private String  checkInStr;
    private String checkOutStr;


    public String getCountryIdEffective() {
        return countryId == null ? "" : countryId;
    }

    public String getProvinceIdEffective() {
        return provinceId == null ? "" : provinceId;
    }

    public String getCityIdEffective() {
        return cityId == null ? "" : cityId;
    }

    public String getHotelIdEffective() {
        return hotelId == null ? "" : hotelId;
    }
}
