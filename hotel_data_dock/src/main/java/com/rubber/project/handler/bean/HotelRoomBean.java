package com.rubber.project.handler.bean;

import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/5/13
 */
@Data
public class HotelRoomBean {
    /**
     * 酒店id
     */
    private String hotelId;

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 房间名称
     */
    private String roomName;


    /**
     * 房间动态的价格房态相关信息
     */
    private List<HotelRoomDynamicBean> hotelRoomDynamicBeans;

}
