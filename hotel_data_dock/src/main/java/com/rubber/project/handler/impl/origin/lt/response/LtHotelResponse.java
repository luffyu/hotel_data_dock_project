package com.rubber.project.handler.impl.origin.lt.response;

import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
@Data
public class LtHotelResponse {

    private String hotelId;

    private String hotelName;

    private String countryId;

    private String countryName;

    private String provinceId;

    private String provinceName;

    private String cityId;

    private String cityName;

    private String address;


    /**
     * 接口状态
     */
    private Integer interfaceStatus = 0;
    /**
     * 信息
     */
    private String msg = "正常";

    /**
     * 房间信息
     */
    private List<LtRoomResponse> roomList;


}
