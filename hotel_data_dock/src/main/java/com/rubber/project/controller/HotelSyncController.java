package com.rubber.project.controller;

import cn.hutool.luffyu.util.result.ResultMsg;
import com.rubber.project.service.HotelDataSyncHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luffyu
 * Created on 2021/4/11
 */
@RestController
@RequestMapping("/room-sync")
public class HotelSyncController {

    @Autowired
    private HotelDataSyncHandlerService hotelDataSyncHandlerService;


    /**
     * 手动执行一次
     */
    @PostMapping("/start-manual/{hotelContrastId}")
    public ResultMsg startForManualV2(@PathVariable("hotelContrastId") Integer hotelContrastId){
        try{
            hotelDataSyncHandlerService.startSync(hotelContrastId);
            return ResultMsg.success();
        }catch (Exception e){
            return ResultMsg.error(e.getMessage());
        }
    }
}
