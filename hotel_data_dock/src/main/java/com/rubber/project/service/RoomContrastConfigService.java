package com.rubber.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.admin.core.base.BaseAdminService;
import com.rubber.project.entity.RoomContrastConfig;
import com.rubber.project.mapper.RoomContrastConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 房间同步配置表 服务实现类
 * </p>
 *
 * @author luffyu-auto
 * @since 2021-04-10
 */
@Service
public class RoomContrastConfigService extends BaseAdminService<RoomContrastConfigMapper, RoomContrastConfig> {


    /**
     * 查询关联的全部酒店信息
     * @param hotelContrastId 当前的酒店信息
     * @return 返回符合要求的酒店信息
     */
    public List<RoomContrastConfig> queryByHotelContrastId(Integer hotelContrastId){
        QueryWrapper<RoomContrastConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hotel_contrast_id",hotelContrastId);
        return list(queryWrapper);
    }


}
