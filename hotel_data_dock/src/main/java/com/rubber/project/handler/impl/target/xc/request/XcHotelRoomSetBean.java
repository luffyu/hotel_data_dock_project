package com.rubber.project.handler.impl.target.xc.request;

import com.rubber.project.handler.impl.target.xc.request.room.XcRoomData;
import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/4/7
 */
@Data
public class XcHotelRoomSetBean {
    /**
     * 代理商id
     */
    private Integer supplierId;

    /**
     * 酒店id
     */
    private Integer hotelId ;


    private List<XcRoomData> roomDataEntitys ;

}
