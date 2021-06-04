package com.rubber.project.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rubber.project.entity.HotelRoomSyncExecWater;
import com.rubber.project.service.HotelRoomSyncExecWaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动清理定时流水
 * @author luffyu
 * Created on 2021/5/16
 */
@Slf4j
@Component
public class AutoClearExecWaterTask {

    /**
     * 保留多少天的流水
     */
    private static final int DAY_OFF_SET = -3;

    @Autowired
    private HotelRoomSyncExecWaterService syncExecWaterService;


    /**
     * 每天凌晨2点多执行一次
     */
    @Scheduled(cron = "0 15 2 * * ?")
    public void handlerTask(){
        log.info("开启执行自动清理执行流水的定时任务");
        doClearExecWater();
    }

    public void doClearExecWater(){
        DateTime dateTime = DateUtil.offsetDay(new Date(), DAY_OFF_SET);
        log.info("开启清除{}之前的流水数据",dateTime.toString());
        try {
            QueryWrapper<HotelRoomSyncExecWater> queryWrapper = new QueryWrapper<>();
            queryWrapper.le("sync_time", dateTime);
            int count = syncExecWaterService.count(queryWrapper);
            boolean remove = false;
            if (count > 0){
                remove = syncExecWaterService.remove(queryWrapper);
            }
            log.info("清除{}之前的{}条流水数据结果为{}",dateTime.toString(),count,remove);
        }catch (Exception e){
            log.info("清除{}之前的流水数据出现异常,msg:{}",dateTime.toString(),e.getMessage());
        }
    }

}
