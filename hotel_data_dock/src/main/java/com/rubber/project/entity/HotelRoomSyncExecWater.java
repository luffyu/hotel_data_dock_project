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
    * 酒店房间的同步流水表
    * </p>
*
* @author luffyu-auto
* @since 2021-04-10
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HotelRoomSyncExecWater extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
    * 自增流水id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 酒店配置的id
     */
    private Integer hotelContrastId;

    /**
    * 房间配置id
    */
    private Integer roomContrastId;

    /**
    * 酒店Id详情 lx To xc
    */
    private String hotelIdInfo;

    /**
    * 房间Id详情 lx To xc
    */
    private String roomIdInfo;

    /**
    * 执行状态 0表示自动同步，1表示手动同步，-1表示不同步
    */
    private Integer execType;

    /**
    * 房间同步的入参JSON
    */
    private String syncRequestInfo;

    /**
    * 房间同步的回参JSON
    */
    private String syncResponseInfo;

    /**
    * 最后一次同步状态 0 表示初始化 1表示成功 2表示失败 3表示异常
    */
    private Integer syncStatus;

    /**
    * 同步时间
    */
    private LocalDateTime syncTime;

    /**
    * 扩展参数
    */
    private String extParam;

    /**
    * 备注信息
    */
    private String remark;

    /**
    * 操作人
    */
    private String operator;

    /**
     * 执行时间
     */
    private Long execTime;

    /**
     * 推送的价格
     */
    private Double pushPrice;

    /**
     * 推送的日期
     */
    private String pushDate;

    /**
     * 推送的状态
     */
    private Integer pushStatus;


}
