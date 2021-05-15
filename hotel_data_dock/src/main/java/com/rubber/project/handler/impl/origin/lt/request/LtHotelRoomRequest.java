package com.rubber.project.handler.impl.origin.lt.request;

import lombok.Data;

import java.util.Date;

/**
 * @author luffyu
 * Created on 2021/4/6
 */
@Data
public class LtHotelRoomRequest extends LtHotelRequest {

    /**
     * 查询开始时间
     */
    private Date checkIn;

    /**
     * 查询结束时间
     */
    private Date checkOut;

    /**
     * 人民币
     */
    private String currency = "CNY";


    /**
     * 是否查询价格
     */
    private boolean ratePlanOnly;


}
