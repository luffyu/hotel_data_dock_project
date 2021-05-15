package com.rubber.project.handler.impl.target.xc.http;

import cn.hutool.core.map.MapUtil;
import cn.hutool.luffyu.util.result.ResultMsg;
import cn.hutool.luffyu.util.result.code.SysCode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rubber.project.exception.HotelDataRequestException;
import com.rubber.project.model.enums.HotelProjectErrCode;
import com.rubber.project.util.Md5Utils;
import com.rubber.project.handler.impl.target.xc.request.HotelRequest;
import com.rubber.project.handler.impl.target.xc.request.XcHotelRoomSetBean;
import com.rubber.project.handler.impl.target.xc.response.XcHotelInfoResponse;
import com.rubber.project.handler.impl.target.xc.response.XcRoomInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
@Slf4j
public class XcHttpUtils {


    private static final String URL = "http://switch.vipdlt.com/apiproxy/soa2/13353/";

    private static final int supplierID = 14123;

    private static final String INTERFACE_KEY = "16c561a9-d8c7-4420-95a1-b7dab8044ff1";


    /**
     * 通过酒店id查询房型id
     * @param hotelId 酒店id
     */
    public static List<XcRoomInfoResponse> getHotelRoomStaticInfo(Integer hotelId) throws Exception {
        HotelRequest request = new HotelRequest();
        request.setSupplierID(supplierID);
        request.setHotelID(hotelId);
        List<String> data = new ArrayList<>();
        data.add("RoomTypeSimpleEntity");
        request.setReturnDataTypeList(data);
        JSONObject params = JSON.parseObject(JSON.toJSONString(request));
        System.out.println(params.toJSONString());
        String urlStr = URL + "gethotelroomstaticinfo";
        String responseStr =  doRequest(urlStr,createHeader(), JSON.parseObject(JSON.toJSONString(request)));
        JSONObject responseJSON = JSON.parseObject(responseStr);
        JSONObject responseStatus = responseJSON.getJSONObject("resultStatus");
        if (responseStatus == null ||  responseStatus.getInteger("resultCode") != 0) {
            throw new HotelDataRequestException(HotelProjectErrCode.REQUEST_ERROR,responseStatus.getString("resultMsg") );
        }
        JSONObject roomStaticInfos = responseJSON.getJSONObject("roomStaticInfos");
        if (roomStaticInfos == null){
            throw new HotelDataRequestException(HotelProjectErrCode.REQUEST_ERROR,"接口gethotelroomstaticinfo数据为空");
        }
        JSONArray arrays = roomStaticInfos.getJSONArray("roomStaticInfo");
        if (arrays == null){
            throw new HotelDataRequestException(HotelProjectErrCode.REQUEST_ERROR,"接口gethotelroomstaticinfo数据的roomStaticInfo为空");
        }
        JSONObject roomInfos = arrays.getJSONObject(0).getJSONObject("roomInfos");
        if (roomInfos == null){
            throw new HotelDataRequestException(HotelProjectErrCode.REQUEST_ERROR,"接口gethotelroomstaticinfo数据的roomInfos为空");
        }
        JSONArray roomInfoArray = roomInfos.getJSONArray("roomInfo");
        return JSONObject.parseArray(roomInfoArray.toJSONString(), XcRoomInfoResponse.class);
    }


    /**
     * 通过酒店id查询房型id
     * @param hotelId 酒店id
     */
    public static XcHotelInfoResponse getHotelInfo(Integer hotelId) throws Exception {
        HotelRequest request = new HotelRequest();
        request.setSupplierID(supplierID);
        request.setHotelID(hotelId);
        String urlStr = URL + "getdlthotellist";
        String hotelDataStr =  doRequest(urlStr,createHeader(), JSON.parseObject(JSON.toJSONString(request)));
        JSONObject responseJSON = JSON.parseObject(hotelDataStr);
        JSONObject responseStatus = responseJSON.getJSONObject("resultStatus");
        if (responseStatus == null ||  responseStatus.getInteger("resultCode") != 0) {
            throw new HotelDataRequestException(HotelProjectErrCode.REQUEST_ERROR,responseStatus.getString("resultMsg") );
        }
        JSONArray hotelArray = responseJSON.getJSONArray("dltHotelEntityList");
        if (hotelArray == null){
            return  null;
        }

        return JSONObject.parseObject(hotelArray.getJSONObject(0).toJSONString(),XcHotelInfoResponse.class);
    }


    /**
     * 推送设置价格
     * @param hotelRoom
     * @return
     * @throws Exception
     */
    public static ResultMsg setRoomPrice(XcHotelRoomSetBean hotelRoom) throws Exception {
        hotelRoom.setSupplierId(supplierID);
        String urlStr = URL + "BatchPushRoomData";
        String responseData =  doRequest(urlStr,createHeader(), JSON.parseObject(JSON.toJSONString(hotelRoom)));
        JSONObject jsonObject = JSON.parseObject(responseData);
        JSONObject resultStatus = jsonObject.getJSONObject("resultStatus");
        boolean success = resultStatus != null && resultStatus.getInteger("resultCode") !=  null && resultStatus.getInteger("resultCode")==0;
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setData(jsonObject);
        if (success){
            resultMsg.setCode(SysCode.SUCCESS.getCode());
        }else {
            resultMsg.setCode(HotelProjectErrCode.REQUEST_ERROR.getCode());
            resultMsg.setMsg(resultStatus != null ? resultStatus.getString("resultMsg"):"error");
        }
        return resultMsg;
    }


    /**
     * 查询当前供应商的全部酒店信息
     */
    public static String getHotelList() throws Exception {
        HotelRequest request = new HotelRequest();
        request.setSupplierID(supplierID);
        String urlStr = URL + "getdlthotellist";
        return doRequest(urlStr,createHeader(), JSON.parseObject(JSON.toJSONString(request)));
    }

    /**
     * 查询城市信息
     */
    public static String getCountryList() throws Exception {
        HotelRequest request = new HotelRequest();
        request.setSupplierID(supplierID);
        String urlStr = URL + "getdltcountrylist";
        return doRequest(urlStr,createHeader(), JSON.parseObject(JSON.toJSONString(request)));
    }

    private static String doRequest(String url,Map<String,String> header,Map<String,Object> data) throws Exception {
        log.info("携程接口BatchPushRoomData请求，request={}",JSON.toJSONString(data));
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
            if (MapUtil.isNotEmpty(header)) {
                for (Map.Entry<String, String> hs : header.entrySet()) {
                    httpPost.setHeader(hs.getKey(), hs.getValue());
                }
            }
            ObjectMapper objectMapper = new ObjectMapper();
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(data),
                    ContentType.create("text/json", "UTF-8")));
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseStr = EntityUtils.toString(entity);
            log.info("携程接口BatchPushRoomData请求返回，response={}",responseStr);
            return responseStr;
        }catch (Exception e){
            log.info("请求接口{} 出现异常，msg={}",url,e.getMessage());
            return null;
        }finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }



    private static Map<String,String> createHeader(){
        Map<String,String> header = new HashMap<>(4);
        long now = System.currentTimeMillis();
        header.put("timestamp",String.valueOf(now));
        String signature = Md5Utils.encoderByMd5(String.valueOf(supplierID) + now + INTERFACE_KEY).toUpperCase();
        header.put("signature",signature);
        header.put("Content-Type","application/json");
        return header;
    }


    public static void main(String[] args) throws Exception {

        //System.out.println(getHotelList());
        System.out.println();
        System.out.println();
        System.out.println();
////        System.out.println(getCountryList());

//        System.out.println();


        //System.out.println(getBasicRoomList(69330011));
        //System.out.println(getHotlRoomStaticInfo(72235126));

//        XcHotelRoomSetBean roomSetBean = new XcHotelRoomSetBean();
//        List<XcRoomData> roomData = new ArrayList<>();
//        roomData.add(new XcRoomData());
//        roomSetBean.setRoomDataEntitys(roomData);
//        System.out.println(setRoomPrice(roomSetBean));

        //System.out.println(getHotlRoomStaticInfo(69330011));

        //System.out.println(getBasicRoomList(72320189));

//        List<XcRoomInfoResponse> data = getHotlRoomStaticInfo(69312428);
//        System.out.println(data);


        System.out.println(getHotelInfo(69312428));

    }

}
