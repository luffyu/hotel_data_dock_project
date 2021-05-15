package com.rubber.project.model;

import com.rubber.project.handler.impl.target.xc.response.XcRoomInfoResponse;
import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/4/9
 * 携程的酒店详情信息
 */
@Data
public class XcHotelInfoModel {

    /**
     * 酒店id
     */
    private Integer hotelId;

    /**
     * 酒店名称
     */
    private String hotelName;

    /**
     * 酒店的房间详细信息
     */
    private List<XcRoomInfoResponse> roomInfoList;

}
