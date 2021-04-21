package com.rubber.project.model;

import com.rubber.admin.core.base.BaseModel;
import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/4/10
 */
@Data
public class HotelSelectModel extends BaseModel {

    /**
     * xc酒店id
     */
    private String xcHotelId;

    /**
     * xc酒店名称
     */
    private String xcHotelName;


    /**
     * lt酒店id
     */
    private String ltHotelId;

    /**
     * lt酒店名称
     */
    private String ltHotelName;


    /**
     * 执行状态 0表示自动执行，1表示手动执行，-1表示不执行
     */
    private Integer execStatus;


}
