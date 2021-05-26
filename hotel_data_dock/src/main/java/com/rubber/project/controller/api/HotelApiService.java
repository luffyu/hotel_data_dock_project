package com.rubber.project.controller.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.luffyu.util.result.ResultMsg;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rubber.admin.core.plugins.page.PageModel;
import com.rubber.project.entity.HotelContrastConfig;
import com.rubber.project.handler.impl.origin.LtOriginDataHandler;
import com.rubber.project.handler.impl.origin.lt.request.LtHotelRequest;
import com.rubber.project.handler.impl.origin.lt.response.LtCityInfoResult;
import com.rubber.project.handler.impl.origin.lt.response.LtHotelSearchListResponse;
import com.rubber.project.handler.impl.origin.lt.response.bean.HotelSearchListBean;
import com.rubber.project.service.HotelContrastConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author luffyu
 * Created on 2021/5/16
 */

@RestController
@RequestMapping("/api")
public class HotelApiService {

    @Autowired
    private LtOriginDataHandler ltOriginDataHandler;

    @Autowired
    private HotelContrastConfigService hotelContrastConfigService;

    @GetMapping("/hotel/lt/search")
    public ResultMsg queryHotelInfo(LtHotelRequest request) throws Exception {
        LtHotelSearchListResponse hotelSearch = ltOriginDataHandler.getHotelSearch(request);
        return ResultMsg.success(hotelSearch.getHotelSearchListBean());
    }


    @GetMapping("/hotel/config/search")
    public ResultMsg searchHotelInfo(String hotelName) throws Exception {
        List<HotelSearchListBean> hotelSearchListBeans = new ArrayList<>();
        //查询配置信息
        if (StrUtil.isNotEmpty(hotelName)){
            List<HotelContrastConfig> hotelContrastConfigs = hotelContrastConfigService.searchConfigHotelByName(hotelName);
            if (CollUtil.isNotEmpty(hotelContrastConfigs)){
                if (hotelContrastConfigs.size() > 10){
                    hotelContrastConfigs = hotelContrastConfigs.subList(0,10);
                }
                LtHotelRequest request = new LtHotelRequest();
                Set<String> collect = hotelContrastConfigs.stream().map(HotelContrastConfig::getLtHotelId).collect(Collectors.toSet());
                String hoteIds = StrUtil.join(",",collect);
                request.setHotelId(hoteIds);
                LtHotelSearchListResponse hotelSearch = ltOriginDataHandler.getHotelSearch(request);
                if (hotelSearch != null){
                    hotelSearchListBeans = hotelSearch.getHotelSearchListBean();
                }
            }
        }
        return ResultMsg.success(hotelSearchListBeans);
    }


    @GetMapping("/static/city")
    public ResultMsg getStaticCity(LtHotelRequest request) throws Exception {
        LtCityInfoResult ltCityInfoResult = ltOriginDataHandler.getLtCityInfo();
        return ResultMsg.success(ltCityInfoResult);
    }



    @GetMapping("/hotel/config/list")
    public ResultMsg getHotelConfigList(PageModel pageModel) throws Exception {
        IPage<HotelContrastConfig> page = new Page<>();
        page.setSize(pageModel.getSize());
        page.setCurrent(pageModel.getPage());
        QueryWrapper<HotelContrastConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("hotel_contrast_id");
        page = hotelContrastConfigService.page(page,queryWrapper);
        return ResultMsg.success(page);
    }

}
