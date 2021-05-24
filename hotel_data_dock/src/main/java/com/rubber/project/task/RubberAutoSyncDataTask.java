package com.rubber.project.task;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.project.config.HotelDataConfig;
import com.rubber.project.entity.HotelContrastConfig;
import com.rubber.project.model.enums.ExecType;
import com.rubber.project.service.HotelContrastConfigService;
import com.rubber.project.service.HotelDataSyncHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/4/13
 */
@Slf4j
@Component
public class RubberAutoSyncDataTask {

    @Autowired
    private HotelDataConfig taskDataConfig;

    @Autowired
    private HotelContrastConfigService hotelContrastConfigService;


    @Autowired
    private HotelDataSyncHandlerService hotelDataSyncHandlerService;


    @Scheduled(cron = "0 0/30 * * * ?")
    public void handlerTask(){
        //doHandlerTask();
    }


    public synchronized void doHandlerTask(){
        log.info("开启执行定时任务");
        if (taskDataConfig.isOpenTask()){
            List<HotelContrastConfig> hotelContrastConfigs = queryAllConfigHotel();
            if (CollUtil.isNotEmpty(hotelContrastConfigs)){
                hotelContrastConfigs.forEach(i->{
                    try {
                        boolean result = hotelDataSyncHandlerService.startSync(i, ExecType.MANUAL_EXEC);
                        log.info("酒店配置{},xc酒店id:{},lt酒店id:{} 执行结果{}",i.getHotelContrastId(),i.getXcHotelId(),i.getLtHotelId(),result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("酒店配置{},xc酒店id:{},lt酒店id:{} 执行异常，msg={}",i.getHotelContrastId(),i.getXcHotelId(),i.getLtHotelId(),e.getMessage());
                    }
                });
            }
        }else {
            log.info("未开启定时任务");
        }
        log.info("执行定时任务完成");
    }


    /**
     * 找出全部的配置酒店
     * @return 开始执行
     */
    private List<HotelContrastConfig> queryAllConfigHotel(){
        QueryWrapper<HotelContrastConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exec_type", ExecType.AUTO_EXEC.getCode());
        return hotelContrastConfigService.list(queryWrapper);
    }

}
