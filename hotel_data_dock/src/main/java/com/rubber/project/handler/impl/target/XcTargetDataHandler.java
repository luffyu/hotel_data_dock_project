package com.rubber.project.handler.impl.target;

import cn.hutool.luffyu.util.result.ResultMsg;
import cn.hutool.luffyu.util.result.code.SysCode;
import com.alibaba.fastjson.JSON;
import com.rubber.project.entity.HotelContrastConfig;
import com.rubber.project.entity.HotelRoomSyncExecWater;
import com.rubber.project.entity.RoomContrastConfig;
import com.rubber.project.handler.TargetDataHandler;
import com.rubber.project.handler.impl.origin.lt.enmus.LtBreakfastEnums;
import com.rubber.project.model.enums.SyncStatus;
import com.rubber.project.handler.impl.target.xc.enmus.XcRoomStatusEnums;
import com.rubber.project.handler.impl.target.xc.http.XcHttpUtils;
import com.rubber.project.handler.impl.target.xc.request.XcHotelRoomSetBean;
import com.rubber.project.handler.impl.target.xc.request.room.XcRoomData;
import com.rubber.project.handler.impl.target.xc.request.room.XcRoomInventory;
import com.rubber.project.handler.impl.target.xc.request.room.XcRoomPrice;
import com.rubber.project.handler.impl.target.xc.request.room.XcRoomStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/5/15
 */
@Component
public class XcTargetDataHandler implements TargetDataHandler {


    /**
     * 代理商id
     */
    private static final int supplierID = 14123;

    private final static int MAX_SET_SIZE = 250;


    /**
     * 写入缓存的数据信息
     *
     * @param contrastConfig
     * @param roomData       房间值
     * @return 返回是否写入成功
     */
    @Override
    public boolean writeToTarget(HotelContrastConfig contrastConfig, Map<RoomContrastConfig, List<HotelRoomSyncExecWater>> roomData) throws Exception {
        XcHotelRoomSetBean xcHotelRoomSetBean = doCreateTargetData(contrastConfig, roomData);

        //存在长度过长的问题
        List<ResultMsg> resultMsg = new ArrayList<>();
        List<XcHotelRoomSetBean> xcHotelRoomSetBeans = new ArrayList<>();
        if (xcHotelRoomSetBean.getRoomDataEntitys().size() > MAX_SET_SIZE) {
            for (int i = 0; i < xcHotelRoomSetBean.getRoomDataEntitys().size(); i += MAX_SET_SIZE) {
                int end = Math.min(i + MAX_SET_SIZE, xcHotelRoomSetBean.getRoomDataEntitys().size());
                XcHotelRoomSetBean roomSetBean = new XcHotelRoomSetBean();
                roomSetBean.setHotelId(xcHotelRoomSetBean.getHotelId());
                roomSetBean.setSupplierId(xcHotelRoomSetBean.getSupplierId());
                roomSetBean.setRoomDataEntitys(xcHotelRoomSetBean.getRoomDataEntitys().subList(i, end));
                xcHotelRoomSetBeans.add(roomSetBean);
            }
        }else {
            xcHotelRoomSetBeans.add(xcHotelRoomSetBean);
        }
        //开始写入结果
        for (XcHotelRoomSetBean roomSetBean:xcHotelRoomSetBeans){
            ResultMsg syncResult = new ResultMsg();
            try {
                syncResult = XcHttpUtils.setRoomPrice(roomSetBean);
            }catch (Exception e){
                syncResult.setCode(String.valueOf(SyncStatus.XC_REQUEST_ERROR.getCode()));
                syncResult.setMsg(e.getMessage());
            }finally {
                resultMsg.add(syncResult);
            }
        }

        ResultMsg syncResult = resultMsg.get(0);
        for (Map.Entry<RoomContrastConfig,List<HotelRoomSyncExecWater>> roomExecWater:roomData.entrySet()){
            List<HotelRoomSyncExecWater> execWaters = roomExecWater.getValue();
            for (HotelRoomSyncExecWater hotelRoomSyncExecWater:execWaters){
                if (!SysCode.SUCCESS.code.equals(syncResult.getCode())){
                    hotelRoomSyncExecWater.setSyncStatus(SyncStatus.XC_REQUEST_ERROR.getCode());
                    hotelRoomSyncExecWater.addRemark(syncResult.getMsg());
                }else {
                    hotelRoomSyncExecWater.setSyncStatus(SyncStatus.SUCCESS.getCode());
                }
                hotelRoomSyncExecWater.setSyncResponseInfo(JSON.toJSONString(syncResult.getData()));
            }
        }
        return true;
    }


    private XcHotelRoomSetBean doCreateTargetData(HotelContrastConfig contrastConfig,Map<RoomContrastConfig, List<HotelRoomSyncExecWater>> roomData){
        XcHotelRoomSetBean xcHotelRoomSetBean = new XcHotelRoomSetBean();
        xcHotelRoomSetBean.setHotelId(contrastConfig.getXcHotelId());
        xcHotelRoomSetBean.setSupplierId(supplierID);

        List<XcRoomData> execXcDataArray = new ArrayList<>();
        for (Map.Entry<RoomContrastConfig,List<HotelRoomSyncExecWater>> roomExecWater:roomData.entrySet()){
            RoomContrastConfig roomContrastConfig = roomExecWater.getKey();
            List<HotelRoomSyncExecWater> execWaters = roomExecWater.getValue();
            for (HotelRoomSyncExecWater hotelRoomSyncExecWater:execWaters){
                XcRoomData xcRoomData = new XcRoomData();

                xcRoomData.setRoomId(roomContrastConfig.getXcRoomId());
                String ltPriceData = hotelRoomSyncExecWater.getPushDate().replaceAll("/", "-");
                xcRoomData.setStartDate(ltPriceData);
                xcRoomData.setEndDate(ltPriceData);
                if (SyncStatus.SUCCESS.getCode().equals(hotelRoomSyncExecWater.getSyncStatus())){
                    XcRoomPrice xcRoomPrice = new XcRoomPrice();
                    xcRoomPrice.setRoomPrice(hotelRoomSyncExecWater.getPushPrice());
                    String breakfast = hotelRoomSyncExecWater.getOriginBean().getBreakfast();
                    LtBreakfastEnums breakfastEnums = LtBreakfastEnums.getByCode(breakfast);
                    xcRoomPrice.setBreakfast(breakfastEnums.getNum());
                    xcRoomData.setRoomPriceModel(xcRoomPrice);

                    XcRoomStatus xcRoomStatus = new XcRoomStatus();
                    xcRoomStatus.setSaleStatus(hotelRoomSyncExecWater.getPushStatus());
                    if (xcRoomStatus.getSaleStatus() == null){
                        xcRoomStatus.setSaleStatus(XcRoomStatusEnums.OPEN.getCode());
                        hotelRoomSyncExecWater.setPushStatus(XcRoomStatusEnums.OPEN.getCode());
                        hotelRoomSyncExecWater.setSyncStatus(SyncStatus.ERROR.getCode());
                        hotelRoomSyncExecWater.setRemark("房态信息异常，设置为关房状态");
                    }
                    xcRoomData.setRoomStatusModel(xcRoomStatus);
                }else {
                    XcRoomStatus xcRoomStatus = new XcRoomStatus();
                    xcRoomStatus.setSaleStatus(XcRoomStatusEnums.OPEN.getCode());
                    xcRoomData.setRoomStatusModel(xcRoomStatus);

                    XcRoomInventory xcRoomInventory = new XcRoomInventory();
                    xcRoomData.setRoomInventoryModel(xcRoomInventory);
                }
                hotelRoomSyncExecWater.setSyncRequestInfo(JSON.toJSONString(xcRoomData));
                execXcDataArray.add(xcRoomData);
            }
        }
        xcHotelRoomSetBean.setRoomDataEntitys(execXcDataArray);
        return xcHotelRoomSetBean;
    }
}
