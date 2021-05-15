package com.rubber.project.handler.impl.target.xc.response;

import lombok.Data;

/**
 * xc返回的酒店信息
 * @author luffyu
 * Created on 2021/4/9
 */
@Data
public class XcRoomInfoResponse {

    /**
     * 酒店id
     */
    private Integer roomId;

    /**
     * 母房间Id
     */
    private Integer basicRoomTypeId;

    /**
     * 房间名称
     */
    private String roomName;


}
