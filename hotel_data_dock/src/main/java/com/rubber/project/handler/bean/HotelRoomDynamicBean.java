package com.rubber.project.handler.bean;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/5/13
 */
@Data
public class HotelRoomDynamicBean {

    /**
     * 房间id
     */
    private String roomId;


    /**
     * 价格计划id
     */
    private String ratePlanId;

    /**
     * 价格计划名称
     */
    private String ratePlanName;


    /**
     * 日期
     */
    private String data;

    /**
     * 早餐数量
     */
    private String breakfast;

    /**
     * 早餐类型
     */
    private String breakfastType;

    /**
     * 价格
     */
    private String price;

    /**
     * 人民币种类
     */
    private String currency;

    /**
     * 房态
     */
    private String statu;

    /**
     * 房间数量
     */
    private String count;

}
