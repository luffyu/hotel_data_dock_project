package com.rubber.project.handler.impl.origin.lt.response.bean;

import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/5/24
 */
@Data
public class HotelSearchListBean {

    private String hotelId;

    private String hotelName;

    private String countryId;

    private String provinceId;

    private String cityId;

    private String cityName;

    private String address;

    private String postCode;

    private String telephone;

    private String email;

    private String startBusinessDate;

    private String repairdate;

    private String score;

    private String star;

    private String lon;

    private String lat;

    private String intro;

    private String guide;

    private String startPrice;

    private String current;

    private List<LandmarkBean> landmarks;


    private List<ServiceShBean> serviceShBeans;

    private List<ImageBean> imageBeans;


}
