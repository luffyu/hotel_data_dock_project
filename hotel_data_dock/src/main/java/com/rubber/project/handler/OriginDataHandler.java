package com.rubber.project.handler;

import com.rubber.project.entity.HotelRoomSyncExecWater;
import com.rubber.project.entity.RoomContrastConfig;
import com.rubber.project.exception.HotelDataRequestException;
import com.rubber.project.handler.request.OriginRequest;
import com.rubber.project.handler.response.OriginResponse;

import java.util.List;
import java.util.Map;

/**
 * 数据来源处理器
 * @author luffyu
 * Created on 2021/5/13
 */
public interface OriginDataHandler {


    /**
     * 获取源数据
     * @param request 当前的请求参数
     * @return 返回数据信息
     */
    OriginResponse getOriginData(OriginRequest request);


    /**
     * 获取源数据 并转换数据
     * @param request 当前的请求参数
     * @param roomContrastConfigs 配置的数据
     * @return 返回数据信息
     */
    Map<RoomContrastConfig,List<HotelRoomSyncExecWater>> getRoomInfoByOriginData(OriginRequest request, List<RoomContrastConfig> roomContrastConfigs) throws HotelDataRequestException;

}
