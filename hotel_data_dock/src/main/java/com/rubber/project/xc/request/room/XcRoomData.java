package com.rubber.project.xc.request.room;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/4/7
 */
@Data
public class XcRoomData {

    /**
     * 房间id
     */
    private Integer roomId ;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 价格信息
     */
    private XcRoomPrice roomPriceModel;

    /**
     * 状态信息
     */
    private XcRoomStatus roomStatusModel ;

    /**
     * 库存信息
     */
    private XcRoomInventory roomInventoryModel;

}
