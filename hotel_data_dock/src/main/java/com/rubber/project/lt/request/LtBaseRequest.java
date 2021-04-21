package com.rubber.project.lt.request;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
@Data
public class LtBaseRequest {

    private String actionName;

    /**
     * 50:默认;40;全部查询;30:分页查询
     */
    private Integer displayReq = 40;

    //DisplayReq 为 50 时,返回的记录数: DisplayReq
    //为 40 时,改节点无效; DisplayReq 为 30 时,每页
    //记录数
    private Integer pageItems ;

    //DisplayReq为30时,分页编号; DisplayReq为40
    //或 50 时无效
    private Integer pageNo;



}
