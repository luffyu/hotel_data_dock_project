package com.rubber.project.handler.impl.origin.lt.response;

import com.rubber.project.handler.impl.origin.lt.response.bean.CountryBean;
import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
@Data
public class LtCityInfoResult extends BaseResponse {

    private List<CountryBean> countryList;
}
