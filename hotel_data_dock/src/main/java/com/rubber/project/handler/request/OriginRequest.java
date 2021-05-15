package com.rubber.project.handler.request;

import com.rubber.project.model.enums.ExecType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author luffyu
 * Created on 2021/5/13
 */
@Data
public class OriginRequest {

    /**
     * 酒店id
     */
    private String hotelId;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;


    private LocalDateTime now;


    private ExecType execType;

}
