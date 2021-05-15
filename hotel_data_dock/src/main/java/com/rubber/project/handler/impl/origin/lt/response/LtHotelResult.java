package com.rubber.project.handler.impl.origin.lt.response;

import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
@Data
public class LtHotelResult extends BaseResponse {

    private Integer hotelCount;

    private List<LtHotelResponse> responseList;
}
