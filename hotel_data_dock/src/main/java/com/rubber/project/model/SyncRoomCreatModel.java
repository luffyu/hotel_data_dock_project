package com.rubber.project.model;

import com.rubber.project.model.enums.ExecType;
import com.rubber.project.model.enums.SyncStatus;
import com.rubber.project.xc.request.room.XcRoomData;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author luffyu
 * Created on 2021/4/10
 */
@Data
public class SyncRoomCreatModel {

    /**
     * 设置状态
     * 1 表示创建成功
     * 2 表示创建失败
     * 3 表示创建异常
     */
    private SyncStatus status;

    /**
     * 执行的类型
     */
    private ExecType execType;

    /**
     * 具体的原因
     */
    private String msg;

    private Integer hotelContrastId;
    private Integer roomContrastId;

    /**
     * 酒店id
     */
    private String lxHotelId;
    private Integer xcHotelId;


    /**
     * 对应的酒店信息
     */
    private LtRoomDict ltRoomDict;


    /**
     * room信息
     */
    private XcRoomData roomData;


    private LocalDateTime now;

    private Long execTime;


    private String responseData;

}
