package com.rubber.project.model;

import com.rubber.project.model.enums.ExecType;
import lombok.Data;

import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/4/7
 */
@Data
public class DataSyncModel {

    private Integer hotelContrastId;

    /**
     * 携程的酒店id
     */
    private Integer xcHotelId;

    /**
     * lx的数据源酒店id
     */
    private String ltHotelId;

    /**
     * 执行的类型
     */
    private ExecType execType;

    /**
     * 数据对照组
     * key值为 xcRoomlId
     * value值 ltRoomId
     */
    private Map<Integer,LtRoomDict> roomIdDict;

}
