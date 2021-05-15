package com.rubber.project.handler.impl.origin.lt.response;

import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/4/6
 */
@Data
public class LtRoomResponse {

    /**
     * 酒店id
     */
    private String hotelId;

    /**
     * 酒店名称
     */
    private String hotelName;

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 房价计划表
     */
    private List<LtRoomPlanResponse>roomPlans;


}
