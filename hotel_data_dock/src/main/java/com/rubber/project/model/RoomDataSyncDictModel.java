package com.rubber.project.model;

import com.rubber.project.entity.RoomContrastConfig;
import com.rubber.project.handler.bean.HotelRoomDynamicBean;
import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/5/14
 */
@Data
public class RoomDataSyncDictModel {
    /**
     * 配置到数据
     */
    private RoomContrastConfig roomContrastConfig;

    /**
     * 价格信息
     */
    private HotelRoomDynamicBean originRoomDynamicData;

}
