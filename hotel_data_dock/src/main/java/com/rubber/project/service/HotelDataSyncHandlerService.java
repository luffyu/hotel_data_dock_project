package com.rubber.project.service;

import cn.hutool.core.date.DateUtil;
import com.rubber.project.entity.HotelContrastConfig;
import com.rubber.project.entity.HotelRoomSyncExecWater;
import com.rubber.project.entity.RoomContrastConfig;
import com.rubber.project.exception.RequestParamsException;
import com.rubber.project.handler.OriginDataHandler;
import com.rubber.project.handler.TargetDataHandler;
import com.rubber.project.handler.request.OriginRequest;
import com.rubber.project.model.enums.ExecType;
import com.rubber.project.model.enums.HotelProjectErrCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/5/13
 */
@Service
public class HotelDataSyncHandlerService {

    @Autowired
    private HotelContrastConfigService hotelContrastConfigService;

    @Autowired
    private RoomContrastConfigService roomContrastConfigService;


    @Autowired
    private OriginDataHandler originDataHandler;

    @Autowired
    private TargetDataHandler targetDataHandler;

    @Autowired
    private HotelRoomSyncExecWaterService hotelRoomSyncExecWaterService;


    public  boolean startSync(Integer hotelContrastId) throws Exception {
        HotelContrastConfig data = hotelContrastConfigService.getById(hotelContrastId);
        if (data == null){
            throw new RequestParamsException(HotelProjectErrCode.HOTEL_NOT_EXIST,"酒店{}不存在",hotelContrastId);
        }
        return startSync(data, ExecType.MANUAL_EXEC);
    }



    public Boolean startSync(HotelContrastConfig contrastConfig, ExecType execType) throws Exception {
        if (contrastConfig == null){
            throw new RequestParamsException(HotelProjectErrCode.HOTEL_NOT_EXIST,null);
        }
        if (ExecType.STOP_EXEC.getCode().equals(contrastConfig.getExecType())){
            throw new RequestParamsException(HotelProjectErrCode.HOTEL_IS_STOP_EXEC,null);
        }
        //获取到所有到房间配置
        List<RoomContrastConfig> roomContrastConfigs = roomContrastConfigService.queryByHotelContrastId(contrastConfig.getHotelContrastId());

        //读取配置信息 可能存在接口异常到情况
        OriginRequest request = new OriginRequest();
        request.setHotelId(contrastConfig.getLtHotelId());
        request.setStartDate(new Date());
        request.setEndDate(DateUtil.offsetDay(new Date(),20));
        request.setNow(LocalDateTime.now());
        request.setExecType(execType);

        Map<RoomContrastConfig, List<HotelRoomSyncExecWater>> roomInfoByOriginData = originDataHandler.getRoomInfoByOriginData(request, roomContrastConfigs);
        //会写数据
        targetDataHandler.writeToTarget(contrastConfig,roomInfoByOriginData);
        //写入流水
        doWriteWater(contrastConfig,roomInfoByOriginData);
        //写入流水
        return true;
    }


    private void doWriteWater(HotelContrastConfig contrastConfig,Map<RoomContrastConfig, List<HotelRoomSyncExecWater>> execWater){
        for (Map.Entry<RoomContrastConfig,List<HotelRoomSyncExecWater>> roomExecWater:execWater.entrySet()) {
            RoomContrastConfig roomContrastConfig = roomExecWater.getKey();
            List<HotelRoomSyncExecWater> roomSyncExecWaters = roomExecWater.getValue();
            hotelRoomSyncExecWaterService.saveBatch(roomSyncExecWaters);

            HotelRoomSyncExecWater syncExecWater = roomSyncExecWaters.get(0);
            roomContrastConfig.setLastSyncTime(syncExecWater.getSyncTime());
            roomContrastConfig.setLastSyncStatus(syncExecWater.getSyncStatus());
            roomContrastConfig.setLastSyncInfo(syncExecWater.getSyncRequestInfo());
            roomContrastConfig.setRemark(syncExecWater.getRemark());
            roomContrastConfigService.updateById(roomContrastConfig);
        }
        //更新 最近一次的执行
        hotelContrastConfigService.updateLastSyncStatus(contrastConfig.getHotelContrastId());
    }
}
