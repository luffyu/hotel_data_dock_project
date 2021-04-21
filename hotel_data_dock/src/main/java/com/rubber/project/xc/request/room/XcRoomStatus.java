package com.rubber.project.xc.request.room;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/4/7
 */
@Data
public class XcRoomStatus {

    /**
     * 房态:0满房 1销售 2限量
     */
    private Integer saleStatus;


    private String channel = "Ctrip";;

}
