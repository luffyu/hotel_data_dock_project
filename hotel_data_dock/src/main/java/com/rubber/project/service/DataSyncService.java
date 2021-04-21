package com.rubber.project.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.luffyu.util.result.ResultMsg;
import cn.hutool.luffyu.util.result.code.SysCode;
import com.alibaba.fastjson.JSON;
import com.rubber.project.config.TaskDataConfig;
import com.rubber.project.entity.HotelContrastConfig;
import com.rubber.project.entity.RoomContrastConfig;
import com.rubber.project.exception.RequestParamsException;
import com.rubber.project.lt.enmus.LtBreakfastEnums;
import com.rubber.project.lt.enmus.LtRoomStatusEnums;
import com.rubber.project.lt.response.LtHotelResponse;
import com.rubber.project.lt.response.LtRoomPlanResponse;
import com.rubber.project.lt.response.LtRoomResponse;
import com.rubber.project.model.DataSyncModel;
import com.rubber.project.model.LtRoomDict;
import com.rubber.project.model.SyncRoomCreatModel;
import com.rubber.project.model.enums.ExecType;
import com.rubber.project.model.enums.HotelProjectErrCode;
import com.rubber.project.model.enums.SyncStatus;
import com.rubber.project.xc.enmus.XcRoomStatusEnums;
import com.rubber.project.xc.http.XcHttpUtils;
import com.rubber.project.xc.request.XcHotelRoomSetBean;
import com.rubber.project.xc.request.room.XcRoomData;
import com.rubber.project.xc.request.room.XcRoomPrice;
import com.rubber.project.xc.request.room.XcRoomStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 数据同步的service
 * @author luffyu
 * Created on 2021/4/7
 */
@Slf4j
@Service
public class DataSyncService {

    @Autowired
    private LtHotelService ltHotelService;

    @Autowired
    private HotelContrastConfigService hotelContrastConfigService;

    @Autowired
    private RoomContrastConfigService roomContrastConfigService;

    @Autowired
    private HotelRoomSyncExecWaterService hotelRoomSyncExecWaterService;

    @Autowired
    private TaskDataConfig taskDataConfig;

    private final static int MAX_SET_SIZE = 250;


    public boolean startSyncDataByContrastId(Integer hotelContrastId) throws RequestParamsException {
        HotelContrastConfig data = hotelContrastConfigService.getById(hotelContrastId);
        if (data == null){
            throw new RequestParamsException(HotelProjectErrCode.HOTEL_NOT_EXIST,"酒店{}不存在",hotelContrastId);
        }
        return startSyncDataByContrast(data, ExecType.MANUAL_EXEC);

    }
    public boolean startSyncDataByContrast(HotelContrastConfig contrastConfig, ExecType execType) throws RequestParamsException {
        if (contrastConfig == null){
            throw new RequestParamsException(HotelProjectErrCode.HOTEL_NOT_EXIST,null);
        }
        if (ExecType.STOP_EXEC.getCode().equals(contrastConfig.getExecType())){
            throw new RequestParamsException(HotelProjectErrCode.HOTEL_IS_STOP_EXEC,null);
        }
        List<RoomContrastConfig> roomContrastConfigs = roomContrastConfigService.queryByHotelContrastId(contrastConfig.getHotelContrastId());
        contrastConfig.setRoomContrastList(roomContrastConfigs);
        DataSyncModel dataSyncModel = createDataSyncModel(contrastConfig);
        dataSyncModel.setExecType(execType);
        return startSyncDataAfterDay(dataSyncModel,taskDataConfig.getAfterDaySize());
    }


    private DataSyncModel createDataSyncModel(HotelContrastConfig hotelContrastConfig){
        DataSyncModel dataSyncModel = new DataSyncModel();
        dataSyncModel.setHotelContrastId(hotelContrastConfig.getHotelContrastId());
        dataSyncModel.setLtHotelId(hotelContrastConfig.getLtHotelId());
        dataSyncModel.setXcHotelId(hotelContrastConfig.getXcHotelId());

        List<RoomContrastConfig> roomContrastList = hotelContrastConfig.getRoomContrastList();
        Map<Integer,LtRoomDict> ltRoomDictMap = new HashMap<>(roomContrastList.size());
        for(RoomContrastConfig roomContrastConfig:roomContrastList){
            Integer xtRoomId = roomContrastConfig.getXcRoomId();
            LtRoomDict ltRoomDict = new LtRoomDict();
            ltRoomDict.setRoomContrastId(roomContrastConfig.getRoomContrastId());
            ltRoomDict.setLtRoomId(roomContrastConfig.getLtRoomId());
            ltRoomDict.setLtPlatKey(roomContrastConfig.getLtRoomPlanKey());
            ltRoomDictMap.put(xtRoomId,ltRoomDict);
        }
        dataSyncModel.setRoomIdDict(ltRoomDictMap);
        return dataSyncModel;
    }


    /**
     * 同步未来多少天的酒店房间信息
     * @param dataSyncModel 当前的同步信息
     * @param x 设置未来多少天的房间信息
     * @return 返回是否同步成功
     */
    public boolean startSyncDataAfterDay(DataSyncModel dataSyncModel,int x){
//        boolean result = true;
//        for (int i=0;i<x;i++){
//            boolean resultDay = startSyncData(dataSyncModel,i);
//            if (!resultDay){
//                result = false;
//            }
//        }
        return startSyncData(dataSyncModel,x);
    }


    /**
     * 同步数据信息
     * @param dataSyncModel
     * @return
     */
    public boolean startSyncData(DataSyncModel dataSyncModel,int afterDay){
        if (MapUtil.isEmpty(dataSyncModel.getRoomIdDict())){
            hotelContrastConfigService.updateLastSyncError(dataSyncModel.getHotelContrastId(),SyncStatus.FIELD,"未配置酒店房间信息");
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        long startTime = System.currentTimeMillis();
        //获取lt数据
        Map<String, LtRoomResponse> ltDataMap ;
        try {
            LtHotelResponse hotelInfoById = ltHotelService.getHotelInfoById(dataSyncModel.getLtHotelId(),afterDay);
            ltDataMap = hotelInfoById.getRoomList().stream().collect(Collectors.toMap(LtRoomResponse::getRoomId, i -> i));
            log.info("获取到龙腾到接口数据为，ltResponse={}",JSON.toJSONString(ltDataMap));
        }catch (Exception e){
            log.error("龙腾接口读取数据异常,e={}",e.getMessage());
            hotelContrastConfigService.updateLastSyncError(dataSyncModel.getHotelContrastId(),SyncStatus.LT_REQUEST_ERROR,e.getMessage());
            return false;
        }

        //创建数据
        List<SyncRoomCreatModel> syncRoomCreatModelList = new ArrayList<>();
        //循环便利需要同步的酒店数据
        for (Map.Entry<Integer, LtRoomDict> roomDict:dataSyncModel.getRoomIdDict().entrySet()){
            Integer xcHotelId = roomDict.getKey();
            LtRoomDict ltRoomDict = roomDict.getValue();

//            LtRoomPlanResponse dictLtPlan = null;
//            LtRoomResponse ltRoomResponse = ltDataMap.get(ltRoomDict.getLtRoomId());
//            if (ltRoomResponse != null && CollUtil.isNotEmpty(ltRoomResponse.getRoomPlans())){
//                if (StrUtil.isEmpty(ltRoomDict.getLtPlatKey())){
//                    dictLtPlan = ltRoomResponse.getRoomPlans().get(0);
//                }else {
//                    Map<String, LtRoomPlanResponse> planMap = ltRoomResponse.getRoomPlans().stream().collect(Collectors.toMap(LtRoomPlanResponse::getRatePlanId, i -> i));
//                    dictLtPlan = planMap.get(ltRoomDict.getLtPlatKey());
//                }
//            }
            List<LtRoomPlanResponse> needPushPlans = new ArrayList<>();
            LtRoomResponse ltRoomResponse = ltDataMap.get(ltRoomDict.getLtRoomId());
            if (ltRoomResponse != null && CollUtil.isNotEmpty(ltRoomResponse.getRoomPlans())){
                //获取到当前到房间计划，同一个计划可能有多个日期
                List<LtRoomPlanResponse> roomPlans = ltRoomResponse.getRoomPlans();
                for (LtRoomPlanResponse roomPlat:roomPlans){
                    if (roomPlat.getRatePlanId().equals(ltRoomDict.getLtPlatKey())){
                        needPushPlans.add(roomPlat);
                    }
                }
            }
            for (LtRoomPlanResponse pushPlan:needPushPlans){
                SyncRoomCreatModel roomCreatModel = doCreateLtSyncRoomInfo(xcHotelId,pushPlan);
                roomCreatModel.setRoomContrastId(ltRoomDict.getRoomContrastId());
                roomCreatModel.setLxHotelId(dataSyncModel.getLtHotelId());
                roomCreatModel.setXcHotelId(dataSyncModel.getXcHotelId());
                roomCreatModel.setLtRoomDict(ltRoomDict);
                roomCreatModel.setHotelContrastId(dataSyncModel.getHotelContrastId());
                syncRoomCreatModelList.add(roomCreatModel);
            }
        }


        ResultMsg syncResult  = null;
        //创建对象
        XcHotelRoomSetBean xcHotelRoomSetBean = doCreatSyncHotelInfo(dataSyncModel.getXcHotelId(), syncRoomCreatModelList);
        if (CollUtil.isNotEmpty(xcHotelRoomSetBean.getRoomDataEntitys())){
            //写入数据
            try {
                if (xcHotelRoomSetBean.getRoomDataEntitys().size() > MAX_SET_SIZE){
                    List<XcHotelRoomSetBean> xcHotelRoomSetBeans = new ArrayList<>();
                    for (int i=0;i<xcHotelRoomSetBean.getRoomDataEntitys().size();i+=MAX_SET_SIZE){
                        int end = Math.min(i+MAX_SET_SIZE,xcHotelRoomSetBean.getRoomDataEntitys().size());
                        XcHotelRoomSetBean roomSetBean = new XcHotelRoomSetBean();
                        roomSetBean.setHotelId(xcHotelRoomSetBean.getHotelId());
                        roomSetBean.setSupplierId(xcHotelRoomSetBean.getSupplierId());
                        roomSetBean.setRoomDataEntitys(xcHotelRoomSetBean.getRoomDataEntitys().subList(i,end));
                        xcHotelRoomSetBeans.add(roomSetBean);
                    }
                    for (XcHotelRoomSetBean roomSetBean:xcHotelRoomSetBeans){
                        syncResult = XcHttpUtils.setRoomPrice(roomSetBean);
                    }
                }else {
                    syncResult = XcHttpUtils.setRoomPrice(xcHotelRoomSetBean);
                }
            } catch (Exception e) {
                log.error("同步携程接口失败，e={}",e.getMessage());
            }
        }
        long endTime = System.currentTimeMillis();

        for (SyncRoomCreatModel syncRoomCreatModel:syncRoomCreatModelList){
            syncRoomCreatModel.setExecTime(endTime - startTime);
            syncRoomCreatModel.setNow(now);
            syncRoomCreatModel.setExecType(dataSyncModel.getExecType());
            if (SyncStatus.SUCCESS == syncRoomCreatModel.getStatus()){
                if (syncResult == null){
                    syncRoomCreatModel.setStatus(SyncStatus.XC_REQUEST_ERROR);
                    syncRoomCreatModel.setMsg(SyncStatus.XC_REQUEST_ERROR.getName());
                }else {
                    syncRoomCreatModel.setResponseData(JSON.toJSONString(syncResult.getData()));
                    if (!SysCode.SUCCESS.code.equals(syncResult.getCode())){
                        syncRoomCreatModel.setStatus(SyncStatus.XC_REQUEST_ERROR);
                        syncRoomCreatModel.setMsg(syncResult.getMsg());
                    }
                }
            }
        }
        //写入流水
        doWriteWater(dataSyncModel,syncRoomCreatModelList);
        return syncResult != null && SysCode.SUCCESS.code.equals(syncResult.getCode());
    }


    private XcHotelRoomSetBean doCreatSyncHotelInfo(Integer hotelId,List<SyncRoomCreatModel> roomCreatModels){
        XcHotelRoomSetBean xcHotelRoomSetBean = new XcHotelRoomSetBean();
        xcHotelRoomSetBean.setHotelId(hotelId);
        if (CollUtil.isNotEmpty(roomCreatModels)){
            List<XcRoomData> roomDataList = roomCreatModels.stream().filter(i-> SyncStatus.SUCCESS.equals(i.getStatus()) ).map(SyncRoomCreatModel::getRoomData).collect(Collectors.toList());
            xcHotelRoomSetBean.setRoomDataEntitys(roomDataList);
        }
        return xcHotelRoomSetBean;
    }

    private SyncRoomCreatModel doCreateLtSyncRoomInfo(Integer roomId, LtRoomPlanResponse roomPlanResponse){
        SyncRoomCreatModel syncRoomCreatModel = new SyncRoomCreatModel();

        //设置基本信息
        XcRoomData xcRoomData = new XcRoomData();
        xcRoomData.setRoomId(roomId);
        syncRoomCreatModel.setRoomData(xcRoomData);

        if (roomPlanResponse == null){
            syncRoomCreatModel.setStatus(SyncStatus.FIELD);
            syncRoomCreatModel.setMsg("无对应的房价信息");
            return syncRoomCreatModel;
        }
        try {
            String priceData = roomPlanResponse.getData();
            String ltPriceData = priceData.replaceAll("/", "-");
            xcRoomData.setStartDate(ltPriceData);
            xcRoomData.setEndDate(ltPriceData);

            //同步房态
            XcRoomStatus xcRoomStatus = new XcRoomStatus();
            String code = roomPlanResponse.getStatu();
            LtRoomStatusEnums ltStatus = LtRoomStatusEnums.getByCode(code);
            if (ltStatus != null) {
                XcRoomStatusEnums xcStatus = XcRoomStatusEnums.getByKey(ltStatus.getKey());
                if (xcStatus != null) {
                    xcRoomStatus.setSaleStatus(xcStatus.getCode());
                }
            }
            if (xcRoomStatus.getSaleStatus() == null){
                syncRoomCreatModel.setStatus(SyncStatus.FIELD);
                syncRoomCreatModel.setMsg("房态信息异常");
                return syncRoomCreatModel;
            }
            xcRoomData.setRoomStatusModel(xcRoomStatus);

            //同步价格
            XcRoomPrice xcRoomPrice = new XcRoomPrice();
            xcRoomPrice.setRoomPrice(Double.valueOf(roomPlanResponse.getPrice()));
            String breakfast = roomPlanResponse.getBreakfast();
            LtBreakfastEnums breakfastEnums = LtBreakfastEnums.getByCode(breakfast);
            xcRoomPrice.setBreakfast(breakfastEnums.getNum());
            xcRoomData.setRoomPriceModel(xcRoomPrice);

//            XcRoomInventory xcRoomInventory = new XcRoomInventory();
//            xcRoomInventory.setPreservedQuantity(Integer.valueOf(roomPlanResponse.getCount()));
//            xcRoomData.setRoomInventoryModel(xcRoomInventory);

            syncRoomCreatModel.setStatus(SyncStatus.SUCCESS);
            syncRoomCreatModel.setMsg("success");
        }catch (Exception e){
            log.error("创建方法信息异常-msg={}",e);
            syncRoomCreatModel.setStatus(SyncStatus.ERROR);
            syncRoomCreatModel.setMsg(e.getMessage());
        }
        return syncRoomCreatModel;
    }


    private void doWriteWater(DataSyncModel dataSyncModel,List<SyncRoomCreatModel> syncRoomCreatModelList){
        hotelRoomSyncExecWaterService.handlerWaterCreateBean(syncRoomCreatModelList);
        //更新 最近一次的执行
        hotelContrastConfigService.updateLastSyncStatus(dataSyncModel.getHotelContrastId());
    }


}
