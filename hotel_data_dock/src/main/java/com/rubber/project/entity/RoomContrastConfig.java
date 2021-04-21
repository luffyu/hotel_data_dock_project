package com.rubber.project.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rubber.admin.core.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
* <p>
    * 房间同步配置表
    * </p>
*
* @author luffyu-auto
* @since 2021-04-10
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoomContrastConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
    * 自增流水id
    */
    @TableId(value = "room_contrast_id", type = IdType.AUTO)
    private Integer roomContrastId;

    /**
    * 酒店同步id
    */
    private Integer hotelContrastId;

    /**
    * xc酒店id
    */
    private Integer xcHotelId;

    /**
    * xc房间id
    */
    private Integer xcRoomId;

    /**
    * xc房间名称
    */
    private String xcRoomName;

    /**
    * xc房间状态，0表示正常 1表示异常
    */
    private Integer xcRoomStatus;

    /**
    * xc房间详细信息的JSON
    */
    private String xcRoomInfo;

    /**
    * lt酒店id
    */
    private String ltHotelId;

    /**
    * lt房间id
    */
    private String ltRoomId;

    /**
    * lt房间名称
    */
    private String ltRoomName;

    /**
    * lt房间状态，0表示正常 1表示异常
    */
    private Integer ltRoomStatus;

    /**
    * lt房间详细信息的JSON
    */
    private String ltRoomInfo;

    /**
    * lt房间的key值
    */
    private String ltRoomPlanKey;

    /**
     * 龙腾房间计划名称
     */
    private String ltRoomPlanName;

    /**
    * 执行状态 0表示自动同步，1表示手动同步，-1表示不同步
    */
    private Integer execType;

    /**
    * 最后一次同步时间
    */
    private LocalDateTime lastSyncTime;

    /**
    * 最后一次同步状态 0 表示初始化 1表示成功 2表示失败 3表示异常
    */
    private Integer lastSyncStatus;

    /**
    * 最后一次同步的信息
    */
    private String lastSyncInfo;

    /**
    * 扩展参数
    */
    private String extParam;

    /**
    * 备注信息
    */
    private String remark;


}
