package com.rubber.project.controller;

import cn.hutool.luffyu.util.result.ResultMsg;
import com.rubber.project.service.DataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author luffyu
 * Created on 2021/4/11
 */
@RestController
@RequestMapping("/room-sync")
public class HotelSyncController {

    @Autowired
    private DataSyncService dataSyncService;


    /**
     * 手动执行一次
     */
    @PostMapping("/start-manual/{hotelContrastId}")
    public ResultMsg startForManual(@PathVariable("hotelContrastId") Integer hotelContrastId){
        try{
            dataSyncService.startSyncDataByContrastId(hotelContrastId);
            return ResultMsg.success();
        }catch (Exception e){
            return ResultMsg.error(e.getMessage());
        }
    }
}
