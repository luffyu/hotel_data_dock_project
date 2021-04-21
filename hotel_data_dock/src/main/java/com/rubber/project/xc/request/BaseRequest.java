package com.rubber.project.xc.request;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
@Data
public class BaseRequest {

    /**
     * 代理商id
     */
    private Integer supplierID;

    private Integer supplierId;

    /**
     * 请求参数
     */
    private Requestor requestor = new Requestor();

    /**
     * 分页参数
     */
    private Pager pager = new Pager();


    @Data
    public static class Pager{

        private Integer pageSize = 10;

        private  Integer pageIndex = 1;

        private boolean isReturnTotalCount = true;

    }


    @Data
    public static class Requestor{

        /**
         * 版本号
         */
        private String invoker = "ZS";
        /**
         * 操作人
         */
        private String operatorName = "ZS";

        /**
         * 本地ip
         */
        private String opClientIP = "127.0.0.1";

        /**
         * 用户的uid
         */
        private Integer userId = 1;

        /**
         * 语言
         */
        private String languageType = "CN";

    }
}
