package com.rubber.project.model;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/4/9
 */
@Data
public class LtRoomDict {

    private Integer roomContrastId;
    /**
     * 龙腾房间id
     */
    private String ltRoomId;

    /**
     * 龙腾的价格计划Id
     */
    private String ltPlatKey;
}
