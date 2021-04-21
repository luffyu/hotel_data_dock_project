package com.rubber.project.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.luffyu.util.result.ResultMsg;
import com.alibaba.fastjson.JSON;
import com.rubber.admin.core.base.BaseAdminController;
import com.rubber.admin.core.plugins.page.PageModel;
import com.rubber.admin.core.plugins.page.SortType;
import com.rubber.project.entity.HotelContrastConfig;
import com.rubber.project.entity.RoomContrastConfig;
import com.rubber.project.model.enums.HotelProjectErrCode;
import com.rubber.project.service.HotelContrastConfigService;
import com.rubber.project.service.RoomContrastConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 酒店同步配置表 前端控制器
 * </p>
 *
 * @author luffyu-auto
 * @since 2021-04-10
 */
@RestController
@RequestMapping("/hotel-config")
public class HotelContrastConfigController extends BaseAdminController {

    @Autowired
    private HotelContrastConfigService hotelContrastConfigService;

    @Autowired
    private RoomContrastConfigService roomContrastConfigService;

    /**
     * 部门的分页查询
     * @param json 部门的查询信息的json字符串
     * @return 返回部门的信息
     */
    @GetMapping("/list")
    public ResultMsg list(String json){
        PageModel pageModel = decodeForJsonString(json);
        pageModel.setSort(new String[]{"hotel_contrast_id"});
        pageModel.setOrder(SortType.desc);
        return ResultMsg.success(hotelContrastConfigService.pageBySelect(pageModel, HotelContrastConfig.class, null));
    }

    /**
     * 部门的分页查询
     * @return 返回部门的信息
     */
    @GetMapping("/info")
    public ResultMsg list(Integer  hotelContrastId)  {
        if (hotelContrastId == null){
            return ResultMsg.create(HotelProjectErrCode.REQUEST_PARAM_ERROR);
        }
        HotelContrastConfig hotelContrastInfo = hotelContrastConfigService.getById(hotelContrastId);
        if (hotelContrastInfo == null){
            return ResultMsg.create(HotelProjectErrCode.HOTEL_NOT_EXIST);
        }
        return ResultMsg.success(hotelContrastInfo);
    }


    /**
     * 部门的分页查询
     * @return 返回部门的信息
     */
    @GetMapping("/infoAndRoom")
    public ResultMsg listAndRoom(Integer  hotelContrastId)  {
        if (hotelContrastId == null){
            return ResultMsg.create(HotelProjectErrCode.REQUEST_PARAM_ERROR);
        }
        HotelContrastConfig hotelContrastInfo = hotelContrastConfigService.getById(hotelContrastId);
        if (hotelContrastInfo == null){
            return ResultMsg.create(HotelProjectErrCode.HOTEL_NOT_EXIST);
        }
        //查询房间的配置信息
        List<RoomContrastConfig> roomContrastConfigs = roomContrastConfigService.queryByHotelContrastId(hotelContrastId);
        hotelContrastInfo.setRoomContrastList(roomContrastConfigs);

        return ResultMsg.success(hotelContrastInfo);
    }


    /**
     * 保存配置的酒店信息
     * @param contrastConfig 部门的查询信息的json字符串
     * @return 返回部门的信息
     */
    @PostMapping("/saveAdnEdit")
    public ResultMsg saveAndEdit(@RequestBody HotelContrastConfig contrastConfig){
        try {
            if (StrUtil.isNotEmpty(contrastConfig.getRoomContrastListStr())){
                contrastConfig.setRoomContrastList(JSON.parseArray(contrastConfig.getRoomContrastListStr(), RoomContrastConfig.class));
            }
            hotelContrastConfigService.saveAndEdit(contrastConfig);
            return ResultMsg.success(contrastConfig);
        }catch (Exception e){
            return ResultMsg.error(e.getMessage());
        }
    }
}
