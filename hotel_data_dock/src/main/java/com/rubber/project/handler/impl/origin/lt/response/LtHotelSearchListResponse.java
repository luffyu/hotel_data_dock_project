package com.rubber.project.handler.impl.origin.lt.response;

import com.rubber.project.handler.impl.origin.lt.response.bean.HotelSearchListBean;
import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/5/24
 */
@Data
public class LtHotelSearchListResponse extends BaseResponse  {

    private List<HotelSearchListBean> hotelSearchListBean;
}
