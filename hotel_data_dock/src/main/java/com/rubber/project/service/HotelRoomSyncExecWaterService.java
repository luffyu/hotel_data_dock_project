package com.rubber.project.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.admin.core.base.BaseAdminService;
import com.rubber.project.entity.HotelRoomSyncExecWater;
import com.rubber.project.mapper.HotelRoomSyncExecWaterMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
