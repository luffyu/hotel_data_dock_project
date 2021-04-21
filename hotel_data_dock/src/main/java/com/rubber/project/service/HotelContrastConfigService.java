package com.rubber.project.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.rubber.admin.core.base.BaseAdminService;
import com.rubber.admin.core.tools.ServletUtils;
import com.rubber.project.entity.HotelContrastConfig;
import com.rubber.project.entity.HotelRoomSyncExecWater;
import com.rubber.project.entity.RoomContrastConfig;
import com.rubber.project.exception.RequestParamsException;
import com.rubber.project.mapper.HotelContrastConfigMapper;
import com.rubber.project.model.enums.HotelProjectErrCode;
import com.rubber.project.model.enums.SyncStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 酒店同步配置表 服务实现类
 * </p>
 *
 * @author luffyu-auto
 * @since 2021-04-10
 */
@Service
public class HotelContrastConfigService extends BaseAdminService<HotelContrastConfigMapper, HotelContrastConfig> {

    @Autowired
    private RoomContrastConfigService roomContrastConfigService;

    @Autowired
    private HotelRoomSyncExecWaterService hotelRoomSyncExecWaterService;


    @Transactional(
            rollbackFor = Exception.class
    )
    public void saveAndEdit(HotelContrastConfig hotelContrast) throws RequestParamsException {
        if (hotelContrast.getHotelContrastId() != null){
            doUpdate(hotelContrast);
        }else {
            doSave(hotelContrast);
        }
    }

    private void doUpdate(HotelContrastConfig requestHotelConfig) throws RequestParamsException {
        HotelContrastConfig dbHotelConfig = getById(requestHotelConfig.getHotelContrastId());
        if (dbHotelConfig == null){
            throw new RequestParamsException(HotelProjectErrCode.HOTEL_NOT_EXIST);
        }
        if (!dbHotelConfig.getLtHotelId().equals(requestHotelConfig.getLtHotelId()) || !dbHotelConfig.getXcHotelId().equals(requestHotelConfig.getXcHotelId())){
            throw new RequestParamsException(HotelProjectErrCode.HOTEL_CONFIG_ID_CANT_CHANGE);
        }
        if (StrUtil.isNotEmpty(requestHotelConfig.getXcHotelName())){
            dbHotelConfig.setXcHotelName(requestHotelConfig.getXcHotelName());
        }
        if (StrUtil.isNotEmpty(requestHotelConfig.getLtHotelName())){
            dbHotelConfig.setLtHotelName(requestHotelConfig.getLtHotelName());
        }
        if (requestHotelConfig.getExecType() != null){
            dbHotelConfig.setExecType(requestHotelConfig.getExecType());
        }
        if (StrUtil.isNotEmpty(requestHotelConfig.getRemark())){
            dbHotelConfig.setRemark(requestHotelConfig.getRemark());
        }
        dbHotelConfig.setModifyTime(LocalDateTime.now());
        dbHotelConfig.setOperator(String.valueOf(ServletUtils.getLoginUserId()));
        if(!updateById(dbHotelConfig)){
            throw new RequestParamsException(HotelProjectErrCode.ERROR);
        }
        doUpdateConfigRoomList(requestHotelConfig);

    }


    private void doUpdateConfigRoomList(HotelContrastConfig requestHotelConfig) throws RequestParamsException {
        if (CollUtil.isNotEmpty(requestHotelConfig.getRoomContrastList())) {
            List<RoomContrastConfig> needAddList = new ArrayList<>();
            for (RoomContrastConfig roomContrastConfig:requestHotelConfig.getRoomContrastList()){
                if (!requestHotelConfig.getLtHotelId().equals(requestHotelConfig.getLtHotelId()) || !requestHotelConfig.getXcHotelId().equals(requestHotelConfig.getXcHotelId())){
                    throw new RequestParamsException(HotelProjectErrCode.REQUEST_PARAM_ERROR);
                }
                if (StrUtil.isEmpty(roomContrastConfig.getLtRoomId()) || StrUtil.isEmpty(roomContrastConfig.getLtRoomPlanKey())){
                    continue;
                }
                if (roomContrastConfig.getRoomContrastId() == null){
                    roomContrastConfig.setHotelContrastId(requestHotelConfig.getHotelContrastId());
                    roomContrastConfig.setExecType(requestHotelConfig.getExecType());
                    needAddList.add(roomContrastConfig);
                }else {
                    //更新操作
                    roomContrastConfigService.updateById(roomContrastConfig);
                }
            }
            roomContrastConfigService.saveBatch(needAddList);
        }
    }


    private void doSave(HotelContrastConfig hotelContrast) throws RequestParamsException {
        hotelContrast.setCreateTime(LocalDateTime.now());
        hotelContrast.setModifyTime(LocalDateTime.now());
        hotelContrast.setOperator(String.valueOf(ServletUtils.getLoginUserId()));
        if(!save(hotelContrast)){
            throw new RequestParamsException(HotelProjectErrCode.ERROR);
        }
        List<RoomContrastConfig> needAdd = new ArrayList<>();
        List<RoomContrastConfig> roomContrastList = hotelContrast.getRoomContrastList();
        if (CollUtil.isNotEmpty(roomContrastList)){
            for (RoomContrastConfig roomContrastConfig:roomContrastList){
                if (StrUtil.isNotEmpty(roomContrastConfig.getLtHotelId()) && StrUtil.isNotEmpty(roomContrastConfig.getLtRoomPlanKey())){
                    roomContrastConfig.setHotelContrastId(hotelContrast.getHotelContrastId());
                    roomContrastConfig.setLtRoomInfo("");
                    needAdd.add(roomContrastConfig);
                }
            }
        }
        roomContrastConfigService.saveBatch(needAdd);
    }


    public void updateLastSyncStatus(Integer hotelContrastId){
        HotelContrastConfig hotelContrastConfig = getById(hotelContrastId);
        if (hotelContrastConfig != null){
            HotelRoomSyncExecWater hotelRoomSyncExecWater = hotelRoomSyncExecWaterService.queryLastHandlerWaterByHotelConfigId(hotelContrastId);
            if (hotelRoomSyncExecWater != null){
                hotelContrastConfig.setLastSyncStatus(hotelRoomSyncExecWater.getSyncStatus());
                hotelContrastConfig.setLastSyncTime(hotelRoomSyncExecWater.getSyncTime());
                hotelContrastConfig.setRemark(hotelRoomSyncExecWater.getRemark());
                updateById(hotelContrastConfig);
            }
        }
    }

    public void updateLastSyncError(Integer hotelContrastId, SyncStatus syncStatus,String remark){
        HotelContrastConfig hotelContrastConfig = getById(hotelContrastId);
        if (hotelContrastConfig != null){
            hotelContrastConfig.setLastSyncStatus(syncStatus.getCode());
            hotelContrastConfig.setLastSyncTime(LocalDateTime.now());
            hotelContrastConfig.setRemark(remark);
            updateById(hotelContrastConfig);
        }
    }
}
