package com.rubber.project.handler.impl.target.xc.request;

import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
@Data
public class HotelRequest extends BaseRequest {

    /**
     * 酒店id
     */
    private Integer hotelID;
    private Integer hotelId;

    /**
     * 酒店名称
     */
    private Integer hotelName;


    private List<String> returnDataTypeList;

}
