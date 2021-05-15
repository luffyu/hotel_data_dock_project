package com.rubber.project.handler.impl.origin.lt.response;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/4/6
 */
@Data
public class LtRoomPlanResponse {

    private String ratePlanId;

    private String ratePlanName;

    /**
     * 早餐
     */
    private String breakfast;

    private String breakfastType;


    private String rateId;

    /**
     * 日期
     */
    private String data;

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
