package com.rubber.project.controller.admin;


import cn.hutool.luffyu.util.result.ResultMsg;
import com.rubber.admin.core.base.BaseAdminController;
import com.rubber.admin.core.plugins.page.PageModel;
import com.rubber.admin.core.plugins.page.SortType;
import com.rubber.project.entity.HotelRoomSyncExecWater;
import com.rubber.project.service.HotelRoomSyncExecWaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 酒店房间的同步流水表 前端控制器
 * </p>
 *
 * @author luffyu-auto
 * @since 2021-04-10
 */
@RestController
@RequestMapping("/room-sync-water")
public class HotelRoomSyncExecWaterController extends BaseAdminController {

    @Autowired
    private HotelRoomSyncExecWaterService hotelRoomSyncExecWaterService;


    /**
     * 部门的分页查询
     * @param json 部门的查询信息的json字符串
     * @return 返回部门的信息
     */
    @GetMapping("/list")
    public ResultMsg list(String json){
        PageModel pageModel = decodeForJsonString(json);
        pageModel.setOrder(SortType.desc);
        pageModel.setSort(new String[]{"id"});

        Set<String> require = new HashSet<>();
        require.add("roomContrastId");
        return ResultMsg.success(hotelRoomSyncExecWaterService.pageBySelect(pageModel, HotelRoomSyncExecWater.class, require));
    }

}
