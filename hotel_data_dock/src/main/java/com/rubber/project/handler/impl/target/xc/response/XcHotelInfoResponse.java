package com.rubber.project.handler.impl.target.xc.response;

import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/4/9
 */
@Data
public class XcHotelInfoResponse {

    /**
     * 酒店id
     */
    private Integer hotelId;
    private Integer hotelID;

    /**
     * 酒店名称
     */
    private String hotelName;

    /**
     * 城市id
     */
    private Integer  cityID;
    private Integer  cityId;

    /**
     * 城市名称
     */
    private String cityName;


    /**
     * 信息
     */
    private String msg = "正常";
    private Integer interfaceStatus = 0;


    public void setHotelID(Integer hotelID) {
        this.hotelID = hotelID;
        this.hotelId = this.hotelID;
    }

    public void setCityID(Integer cityID) {
        this.cityID = cityID;
        this.cityId = cityID;
    }

    /**
     * 房间信息
     */
    private List<XcRoomInfoResponse> roomInfoList;
}
