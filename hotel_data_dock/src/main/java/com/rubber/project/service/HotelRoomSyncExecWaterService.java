package com.rubber.project.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.admin.core.base.BaseAdminService;
import com.rubber.admin.core.tools.ServletUtils;
import com.rubber.project.entity.HotelRoomSyncExecWater;
import com.rubber.project.entity.RoomContrastConfig;
import com.rubber.project.mapper.HotelRoomSyncExecWaterMapper;
import com.rubber.project.model.LtRoomDict;
import com.rubber.project.model.SyncRoomCreatModel;
import com.rubber.project.model.enums.ExecType;
import com.rubber.project.model.enums.SyncStatus;
import com.rubber.project.xc.request.room.XcRoomData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 酒店房间的同步流水表 服务实现类
 * </p>
 *
 * @author luffyu-auto
 * @since 2021-04-10
 */
@Service
public class HotelRoomSyncExecWaterService extends BaseAdminService<HotelRoomSyncExecWaterMapper, HotelRoomSyncExecWater>  {

    @Autowired
    private RoomContrastConfigService roomContrastConfigService;


    public HotelRoomSyncExecWater queryLastHandlerWaterByHotelConfigId(Integer hotelContrastId){
        QueryWrapper<HotelRoomSyncExecWater> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hotel_contrast_id",hotelContrastId);
        queryWrapper.orderByDesc("id");
        queryWrapper.last(" limit 1");

        List<HotelRoomSyncExecWater> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }




    public void handlerWaterCreateBean(List<SyncRoomCreatModel> syncRoomCreatModelList){
        List<HotelRoomSyncExecWater> hotelRoomSyncExecWaters = doCreateBeanListWater(syncRoomCreatModelList);
        if (CollUtil.isNotEmpty(hotelRoomSyncExecWaters)) {
            Map<Integer,HotelRoomSyncExecWater> last = new HashMap<>();
            for (HotelRoomSyncExecWater hotelRoomSyncExecWater:hotelRoomSyncExecWaters){
                try{
                    save(hotelRoomSyncExecWater);
                    last.put(hotelRoomSyncExecWater.getRoomContrastId(),hotelRoomSyncExecWater);
                }catch (Exception e){
                    log.error("房间流水写入异常");
                }
            }
            for(HotelRoomSyncExecWater lastData:last.values()){
                doUpdateRoomConfigSyncInfo(lastData);
            }
        }
    }

    private void doUpdateRoomConfigSyncInfo(HotelRoomSyncExecWater hotelRoomSyncExecWater){
        if (hotelRoomSyncExecWater.getRoomContrastId() != null){
            RoomContrastConfig roomContrastConfig = roomContrastConfigService.getById(hotelRoomSyncExecWater.getRoomContrastId());
            if (roomContrastConfig != null){
                String syncRequestInfo = hotelRoomSyncExecWater.getSyncRequestInfo();
                if (StrUtil.isNotEmpty(syncRequestInfo)){
                    JSONObject jsonObject = JSONObject.parseObject(syncRequestInfo);
                    if (hotelRoomSyncExecWater.getFloatPrice() != null){
                        jsonObject.put("floatPrice",hotelRoomSyncExecWater.getFloatPrice());
                    }
                    if (hotelRoomSyncExecWater.getFloatType() != null){
                        jsonObject.put("floatType",hotelRoomSyncExecWater.getFloatType());
                    }
                    syncRequestInfo = jsonObject.toJSONString();
                }
                roomContrastConfig.setLastSyncInfo(syncRequestInfo);
                roomContrastConfig.setLastSyncStatus(hotelRoomSyncExecWater.getSyncStatus());
                roomContrastConfig.setLastSyncTime(hotelRoomSyncExecWater.getSyncTime());
                roomContrastConfig.setRemark(hotelRoomSyncExecWater.getRemark());
                roomContrastConfigService.updateById(roomContrastConfig);
            }
        }


    }



    private List<HotelRoomSyncExecWater> doCreateBeanListWater(List<SyncRoomCreatModel> syncRoomCreatModelList){
        List<HotelRoomSyncExecWater> syncRoomCreatModels = new ArrayList<>();
        for (SyncRoomCreatModel syncRoomCreatModel:syncRoomCreatModelList){
            syncRoomCreatModels.add(doCreateBean(syncRoomCreatModel));
        }
        return syncRoomCreatModels;
    }

    private HotelRoomSyncExecWater doCreateBean(SyncRoomCreatModel syncRoomCreatModel){
        HotelRoomSyncExecWater roomSyncExecWater = new HotelRoomSyncExecWater();
        try {
            roomSyncExecWater.setRoomContrastId(syncRoomCreatModel.getRoomContrastId());
            roomSyncExecWater.setHotelIdInfo(syncRoomCreatModel.getLxHotelId() + " to " + syncRoomCreatModel.getXcHotelId());

            String xcRoomId = "";
            String requestInfo = "";
            XcRoomData roomData = syncRoomCreatModel.getRoomData();
            if (roomData != null) {
                xcRoomId = String.valueOf(roomData.getRoomId());
                requestInfo = JSON.toJSONString(syncRoomCreatModel.getRoomData());

                roomSyncExecWater.setPushPrice(roomData.getRoomPriceModel() == null? 0.0:roomData.getRoomPriceModel().getRoomPrice());
                roomSyncExecWater.setPushDate(roomData.getStartDate());
                roomSyncExecWater.setPushStatus(roomData.getRoomStatusModel() == null ? -1:roomData.getRoomStatusModel().getSaleStatus());
            }

            String ltRoomId = "";
            LtRoomDict ltRoomDict = syncRoomCreatModel.getLtRoomDict();
            if (ltRoomDict != null) {
                ltRoomId = ltRoomDict.getLtRoomId() + "_" + ltRoomDict.getLtPlatKey();
                roomSyncExecWater.setFloatPrice(ltRoomDict.getFloatPrice());
                roomSyncExecWater.setFloatType(ltRoomDict.getFloatType());
            }
            roomSyncExecWater.setOriginPrice(syncRoomCreatModel.getOriginPrice());
            roomSyncExecWater.setRoomIdInfo(ltRoomId + " to " + xcRoomId);
            roomSyncExecWater.setSyncRequestInfo(requestInfo);

            roomSyncExecWater.setHotelContrastId(syncRoomCreatModel.getHotelContrastId());

            roomSyncExecWater.setExecType(syncRoomCreatModel.getExecType().getCode());
            roomSyncExecWater.setSyncTime(syncRoomCreatModel.getNow());

            roomSyncExecWater.setSyncStatus(syncRoomCreatModel.getStatus().getCode());
            roomSyncExecWater.setRemark(syncRoomCreatModel.getMsg());

            roomSyncExecWater.setSyncResponseInfo(syncRoomCreatModel.getResponseData());

            roomSyncExecWater.setExecTime(syncRoomCreatModel.getExecTime());
            roomSyncExecWater.setExtParam("");
            if (ExecType.AUTO_EXEC.equals(syncRoomCreatModel.getExecType())) {
                roomSyncExecWater.setOperator("system");
            } else {
                roomSyncExecWater.setOperator(String.valueOf(ServletUtils.getLoginUserId()));
            }
        }catch (Exception e){
            roomSyncExecWater.setSyncStatus(SyncStatus.ERROR.getCode());
            roomSyncExecWater.setRemark(e.getMessage());
        }
        return roomSyncExecWater;
    }


}
