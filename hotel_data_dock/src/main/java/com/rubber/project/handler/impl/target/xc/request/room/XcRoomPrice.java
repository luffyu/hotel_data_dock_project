package com.rubber.project.handler.impl.target.xc.request.room;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/4/7
 */
@Data
public class XcRoomPrice {

    /**
     * 房间价格
     */
    private Double roomPrice;

    private String currency = "CNY";

    private Integer breakfast;

    private String channel = "Ctrip";
}
