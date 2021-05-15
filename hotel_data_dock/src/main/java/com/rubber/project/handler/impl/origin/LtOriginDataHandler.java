package com.rubber.project.handler.impl.origin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.rubber.admin.core.tools.ServletUtils;
import com.rubber.project.entity.HotelRoomSyncExecWater;
import com.rubber.project.entity.RoomContrastConfig;
import com.rubber.project.exception.HotelDataRequestException;
import com.rubber.project.exception.RequestParamsException;
import com.rubber.project.handler.OriginDataHandler;
import com.rubber.project.handler.bean.HotelRoomBean;
import com.rubber.project.handler.bean.HotelRoomDynamicBean;
import com.rubber.project.handler.request.OriginRequest;
import com.rubber.project.handler.response.OriginResponse;
import com.rubber.project.handler.impl.origin.lt.enmus.LtRoomStatusEnums;
import com.rubber.project.handler.impl.origin.lt.http.AsmxHttpUtil;
import com.rubber.project.handler.impl.origin.lt.request.LtHotelRoomRequest;
import com.rubber.project.handler.impl.origin.lt.response.LtHotelResponse;
import com.rubber.project.handler.impl.origin.lt.response.LtHotelResult;
import com.rubber.project.handler.impl.origin.lt.response.LtRoomPlanResponse;
import com.rubber.project.handler.impl.origin.lt.response.LtRoomResponse;
import com.rubber.project.model.enums.HotelProjectErrCode;
import com.rubber.project.model.enums.PriceFloatTypeEnums;
import com.rubber.project.model.enums.SyncStatus;
import com.rubber.project.handler.impl.target.xc.enmus.XcRoomStatusEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author luffyu
 * Created on 2021/5/13
 */
@Slf4j
@Service
public class LtOriginDataHandler implements OriginDataHandler {


    /**
     * 获取源数据
     *
     * @param request 当前的请求参数
     * @return 返回数据信息
     */
    @Override
    public OriginResponse getOriginData(OriginRequest request) {
        OriginResponse response = new OriginResponse();
        LtHotelResponse ltHotelResponse = null;
        try {
            ltHotelResponse = getHotelInfoById(request);
        }catch (Exception e) {
            log.error("调用龙腾的接口错误，msg:{}",e.getMessage());
            response.setInterfaceStatus(SyncStatus.LT_REQUEST_ERROR.getCode());
            response.setMsg(e.getMessage());
        }
        if (ltHotelResponse != null){
            //清理数据
            handlerClearData(response,ltHotelResponse);
        }
        return response;
    }

    /**
     * 获取源数据 并转换数据
     *
     * @param request             当前的请求参数
     * @param roomContrastConfigs 配置的数据
     * @return 返回数据信息
     */
    @Override
    public Map<RoomContrastConfig,List<HotelRoomSyncExecWater>> getRoomInfoByOriginData(OriginRequest request, List<RoomContrastConfig> roomContrastConfigs) throws HotelDataRequestException {
        OriginResponse originData = getOriginData(request);
        //转换成map结构
        if (originData.getInterfaceStatus() != 0) {
            throw new HotelDataRequestException(String.valueOf(originData.getInterfaceStatus()),originData.getMsg(),null);
        }
        Map<RoomContrastConfig,List<HotelRoomSyncExecWater>> syncDataMap = new HashMap<>(roomContrastConfigs.size());
        //表示成功
        Map<String,HotelRoomBean> hotelRoomBeans = originData.getHotelRoomBeans().stream().collect(Collectors.toMap(HotelRoomBean::getRoomId, i->i));
        Set<String> dateMap = doGetDateMap(request);
        for (RoomContrastConfig roomContrastConfig:roomContrastConfigs){
            HotelRoomBean hotelRoomBean = hotelRoomBeans.get(roomContrastConfig.getLtRoomId());
            if (hotelRoomBean == null){
                roomContrastConfig.setLtRoomStatus(SyncStatus.LT_ROOM_IS_NULL.getCode());
                roomContrastConfig.setRemark(SyncStatus.LT_ROOM_IS_NULL.getName());
                continue;
            }
            Map<String, HotelRoomDynamicBean> collect = hotelRoomBean.getHotelRoomDynamicBeans().stream().collect(Collectors.toMap(j-> j.getRatePlanId()+"_"+j.getData(), i -> i));
            List<HotelRoomSyncExecWater> hotelRoomSyncExecWaters = new ArrayList<>();
            //多个日期的价格 按照天读取日期
            for (String date:dateMap){
                String key = roomContrastConfig.getLtRoomPlanKey() + "_" + date;
                HotelRoomDynamicBean hotelRoomDynamicBean = collect.get(key);
                HotelRoomSyncExecWater hotelRoomSyncExecWater = doCreatHotelRoomSyncExecWater(request, roomContrastConfig, hotelRoomDynamicBean);
                hotelRoomSyncExecWater.setPushDate(date);
                hotelRoomSyncExecWaters.add(hotelRoomSyncExecWater);
            }
            syncDataMap.put(roomContrastConfig,hotelRoomSyncExecWaters);
        }
        return syncDataMap;

    }


    private HotelRoomSyncExecWater doCreatHotelRoomSyncExecWater(OriginRequest request,RoomContrastConfig roomContrastConfig,HotelRoomDynamicBean hotelRoomDynamicBean){
        HotelRoomSyncExecWater roomSyncExecWater = new HotelRoomSyncExecWater();
        roomSyncExecWater.setHotelContrastId(roomContrastConfig.getHotelContrastId());
        roomSyncExecWater.setRoomContrastId(roomContrastConfig.getRoomContrastId());
        roomSyncExecWater.setHotelIdInfo(roomContrastConfig.getLtHotelId() + " to " + roomContrastConfig.getXcHotelId());
        roomSyncExecWater.setRoomIdInfo(roomContrastConfig.getLtRoomId()+"_" + roomContrastConfig.getLtRoomPlanKey() + " to " + roomContrastConfig.getXcRoomId());
        roomSyncExecWater.setExtParam("");
        roomSyncExecWater.setSyncTime(request.getNow());
        roomSyncExecWater.setExecType(request.getExecType().getCode());
        Integer uid = ServletUtils.getLoginUserId();
        roomSyncExecWater.setOperator(String.valueOf(uid == null ? "system" : uid));
        if (hotelRoomDynamicBean == null){
            roomSyncExecWater.setSyncRequestInfo("");
            roomSyncExecWater.setSyncStatus(SyncStatus.LT_ROOM_PRICE_IS_NULL.getCode());
            roomSyncExecWater.setRemark(SyncStatus.LT_ROOM_PRICE_IS_NULL.getName()+",采用默认策略");
            roomSyncExecWater.setSyncTime(request.getNow());
            roomSyncExecWater.setPushStatus(XcRoomStatusEnums.OPEN.getCode());
            return roomSyncExecWater;
        }
        LtRoomStatusEnums ltStatus = LtRoomStatusEnums.getByCode(hotelRoomDynamicBean.getStatu());
        if (ltStatus != null) {
            XcRoomStatusEnums xcStatus = XcRoomStatusEnums.getByKey(ltStatus.getKey());
            if (xcStatus != null) {
                roomSyncExecWater.setPushStatus(xcStatus.getCode());
            }
        }
        roomSyncExecWater.setOriginBean(hotelRoomDynamicBean);
        roomSyncExecWater.setFloatPrice(roomContrastConfig.getFloatPrice());
        roomSyncExecWater.setFloatType(roomContrastConfig.getFloatType());
        roomSyncExecWater.setOriginPrice(Double.valueOf(hotelRoomDynamicBean.getPrice()));
        if (PriceFloatTypeEnums.ADD.getKey().equals(roomSyncExecWater.getFloatType())){
            roomSyncExecWater.setPushPrice(roomSyncExecWater.getOriginPrice() + roomSyncExecWater.getFloatPrice());
        }
        if (PriceFloatTypeEnums.REDUCE.getKey().equals(roomSyncExecWater.getFloatType())){
            roomSyncExecWater.setPushPrice(roomSyncExecWater.getOriginPrice() - roomSyncExecWater.getFloatPrice());
        }
        roomSyncExecWater.setSyncStatus(SyncStatus.SUCCESS.getCode());
        roomSyncExecWater.setRemark(SyncStatus.SUCCESS.getName());
        return roomSyncExecWater;
    }




    private void handlerClearData(OriginResponse response,LtHotelResponse ltHotelResponse){
        response.setHotelId(ltHotelResponse.getHotelId());
        response.setHotelName(ltHotelResponse.getHotelName());
        List<HotelRoomBean> hotelRoomBeans = new ArrayList<>();
        for (LtRoomResponse ltRoomResponse:ltHotelResponse.getRoomList()){
            HotelRoomBean hotelRoomBean = new HotelRoomBean();

            hotelRoomBean.setHotelId(ltHotelResponse.getHotelId());
            hotelRoomBean.setRoomId(ltRoomResponse.getRoomId());
            hotelRoomBean.setRoomName(ltRoomResponse.getRoomName());
            List<HotelRoomDynamicBean> hotelRoomDynamicBeanList = new ArrayList<>();

            for (LtRoomPlanResponse ltRoomPlanResponse :ltRoomResponse.getRoomPlans()){
                HotelRoomDynamicBean roomDynamicBean = new HotelRoomDynamicBean();
                BeanUtils.copyProperties(ltRoomPlanResponse,roomDynamicBean);
                hotelRoomDynamicBeanList.add(roomDynamicBean);
            }
            hotelRoomBean.setHotelRoomDynamicBeans(hotelRoomDynamicBeanList);
            hotelRoomBeans.add(hotelRoomBean);
        }
        response.setHotelRoomBeans(hotelRoomBeans);
    }



    /**
     * 通过酒店id 查询龙腾的接口数据信息
     */
    private LtHotelResponse getHotelInfoById(OriginRequest request) throws Exception {
        if (request ==null || StrUtil.isEmpty(request.getHotelId())){
            throw new RequestParamsException(HotelProjectErrCode.REQUEST_PARAM_ERROR,"酒店id不能为空");
        }
        LtHotelRoomRequest ltRequest = new LtHotelRoomRequest();
        ltRequest.setHotelId(request.getHotelId());
        ltRequest.setCheckIn(request.getStartDate());
        ltRequest.setCheckOut(request.getEndDate());
        LtHotelResult ltHotelResult = AsmxHttpUtil.getHotelInfoPrice(ltRequest);
        if ( !"30000".equals(ltHotelResult.getCode()) || CollUtil.isEmpty(ltHotelResult.getResponseList())){
            log.error("调用龙腾的接口异常，code= {} msg={}",ltHotelResult.getCode(),ltHotelResult.getDescription());
            String msg = ltHotelResult.getCode() + ":" + ltHotelResult.getDescription();
            throw new HotelDataRequestException(HotelProjectErrCode.REQUEST_ERROR,msg);
        }
        return ltHotelResult.getResponseList().get(0);
    }



    private Set<String> doGetDateMap(OriginRequest request){
        Set<String> dateStr = new LinkedHashSet<>();
        for (Date startDate = request.getStartDate(); DateUtil.compare(startDate,request.getEndDate()) < 0 ; startDate = DateUtil.offsetDay(startDate,1)){
            //循环便利查询的日期
            dateStr.add(DateUtil.format(startDate,"yyyy/MM/dd"));
        }
        return dateStr;
    }
}
