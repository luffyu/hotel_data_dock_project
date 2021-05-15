package com.rubber.project.handler.bean;

import com.rubber.project.model.enums.SyncStatus;
import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/5/13
 */
@Data
public class BaseRemoteResponse {

    /**
     * 接口状态
     * @see SyncStatus
     */
    private Integer interfaceStatus = 0;

    /**
     * 信息
     */
    private String msg = "正常";
}
