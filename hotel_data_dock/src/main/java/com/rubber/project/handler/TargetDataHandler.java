package com.rubber.project.handler;

import com.rubber.project.entity.HotelContrastConfig;
import com.rubber.project.entity.HotelRoomSyncExecWater;
import com.rubber.project.entity.RoomContrastConfig;

import java.util.List;
import java.util.Map;

/**
 *
 * 目标数据 设置处理器
 * @author luffyu
 * Created on 2021/5/13
 */
public interface TargetDataHandler {


    /**
     * 写入缓存的数据信息
     * @param roomData 房间值
     * @return 返回是否写入成功
     */
    boolean writeToTarget(HotelContrastConfig contrastConfig, Map<RoomContrastConfig, List<HotelRoomSyncExecWater>> roomData) throws Exception;

}
