package com.rubber.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rubber.admin.core.base.BaseEntity;
import com.rubber.project.model.enums.ExecType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
* <p>
    * 酒店同步配置表
    * </p>
*
* @author luffyu-auto
* @since 2021-04-10
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HotelContrastConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
    * 自增流水id
    */
    @TableId(value = "hotel_contrast_id", type = IdType.AUTO)
    private Integer hotelContrastId;

    /**
    * xc酒店id
    */
    private Integer xcHotelId;

    /**
    * xc酒店名称
    */
    private String xcHotelName;

    /**
    * xc接口状态，0表示正常 1表示异常
    */
    private Integer xcRequestStatus;

    /**
    * xc酒店详细信息的JSON
    */
    private String xcHotelInfo;

    /**
    * lt酒店id
    */
    private String ltHotelId;

    /**
    * lt酒店名称
    */
    private String ltHotelName;

    /**
    * lt接口状态，0表示正常 1表示异常
    */
    private Integer ltRequestStatus;

    /**
    * lt酒店详细信息的JSON
    */
    private String ltHotelInfo;

    /**
    * 执行状态 0表示自动执行，1表示手动执行，-1表示不执行
    */
    private Integer execType;

    /**
    * 执行间隔时间(分钟) 最小为1
    */
    private Integer execIntervalTime;

    /**
    * 最后一次同步时间
    */
    private LocalDateTime lastSyncTime;

    /**
    * 最后一次同步状态 0表示正常 1表示异常
    */
    private Integer lastSyncStatus;

    /**
    * 备注信息
    */
    private String remark;

    /**
    * 扩展参数
    */
    private String extParam;

    /**
    * 版本号
    */
    private Integer version;

    /**
    * 创建时间
    */
    private LocalDateTime createTime;

    /**
    * 修改时间
    */
    private LocalDateTime modifyTime;

    /**
    * 操作人
    */
    private String operator;


    /**
     * 酒店配置的房间信息
     */
    @TableField(exist = false)
    private List<RoomContrastConfig> roomContrastList;


    /**
     * 酒店配置的房间信息
     */
    @TableField(exist = false)
    private String roomContrastListStr;

    /**
     * 当前执行的类型
     */
    @TableField(exist = false)
    private ExecType handlerExecType;

}
