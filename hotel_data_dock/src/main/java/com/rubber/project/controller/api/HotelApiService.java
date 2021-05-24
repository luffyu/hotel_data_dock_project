package com.rubber.project.controller.api;

import cn.hutool.luffyu.util.result.ResultMsg;
import com.rubber.project.handler.impl.origin.LtOriginDataHandler;
import com.rubber.project.handler.impl.origin.lt.request.LtHotelRequest;
import com.rubber.project.handler.impl.origin.lt.response.LtCityInfoResult;
import com.rubber.project.handler.impl.origin.lt.response.LtHotelSearchListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author luffyu
 * Created on 2021/5/16
 */

@RestController
@RequestMapping("/api")
public class HotelApiService {

    @Autowired
    private LtOriginDataHandler ltOriginDataHandler;


    @GetMapping("/hotel/search")
    public ResultMsg queryHotelInfo(LtHotelRequest request) throws Exception {
        LtHotelSearchListResponse hotelSearch = ltOriginDataHandler.getHotelSearch(request);
        return ResultMsg.success(hotelSearch);
    }


    @GetMapping("/static/city")
    public ResultMsg getStaticCity(LtHotelRequest request) throws Exception {
        LtCityInfoResult ltCityInfoResult = ltOriginDataHandler.getLtCityInfo();
        return ResultMsg.success(ltCityInfoResult);
    }

}
