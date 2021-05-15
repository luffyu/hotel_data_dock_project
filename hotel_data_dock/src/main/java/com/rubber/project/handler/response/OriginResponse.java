package com.rubber.project.handler.response;

import com.rubber.project.handler.bean.BaseRemoteResponse;
import com.rubber.project.handler.bean.HotelRoomBean;
import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/5/13
 */
@Data
public class OriginResponse extends BaseRemoteResponse {

    /**
     * 酒店id
     */
    private String hotelId;

    /**
     * 酒店名称
     */
    private String hotelName;


    /**
     * 房间的相关信息
     */
    private List<HotelRoomBean> hotelRoomBeans;


}
