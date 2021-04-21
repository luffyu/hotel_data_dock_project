package com.rubber.project.lt.http;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.rubber.project.lt.request.LtBaseRequest;
import com.rubber.project.lt.request.LtHotelRequest;
import com.rubber.project.lt.request.LtHotelRoomRequest;
import com.rubber.project.lt.resolve.HotelResponseResolve;
import com.rubber.project.lt.resolve.HotelRoomResponseResolve;
import com.rubber.project.lt.response.LtHotelResult;
import org.apache.axis.Message;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.MessageElement;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.soap.SOAPBody;
import java.util.Date;


/**
 * @author luffyu
 * Created on 2021/4/5
 */
public class AsmxHttpUtil {

    /**
     * 龙腾的相关接口参数
     * 请求的参数
     * 可以通过 http://202.104.101.32:8077/RESTServer.asmx?wsdl 来查看暴露的服务
     * 正式环境地址为：http://lvtianxia.dingfangyi.com/RESTSERVER.asmx
     */
    private final static String URL = "http://202.104.101.32:8077/RESTServer.asmx";
    //private final static String URL = "http://lvtianxia.dingfangyi.com/RESTSERVER.asmx";
    private final static String SOAP_ACTION = "http://tempuri.org/GetXmlData";
    private final static String TARGET_NAMESPACE = "http://tempuri.org/";
    private final static String OPTION_NAME = "GetXmlData";
    private final static String XML_REQUEST  = "xmlRequest";


    public static LtHotelResult getHotelSearch(LtHotelRequest request) throws Exception {
        //设置默认的值
        request.setActionName("HotelSearch");
        Document document = getDefaultHandlerData(request);
        Element root = document.getRootElement();
        Element searchConditions = root.addElement("SearchConditions");
        searchConditions.addElement("CountryId").setText(request.getCountryIdEffective());
        searchConditions.addElement("ProvinceId").setText(request.getProvinceIdEffective());
        searchConditions.addElement("CityId").setText(request.getCityIdEffective());
        searchConditions.addElement("HotelId").setText(request.getHotelIdEffective());
        searchConditions.addElement("Lang").setText("GB");
        MessageElement response =  doRequest(document.asXML());
        return HotelResponseResolve.resolve(response);
    }

    /**
     * 查询酒店的各个房间的价格信息
     */
    public static LtHotelResult getHotelInfoPrice(LtHotelRoomRequest request) throws Exception {
        //输出一个酒店对象
        request.setActionName("RatePlanSearch");
        Document document = getDefaultHandlerData(request);
        Element root = document.getRootElement();
        Element searchConditions = root.addElement("SearchConditions");
        searchConditions.addElement("CountryId").setText(request.getCountryIdEffective());
        searchConditions.addElement("ProvinceId").setText(request.getProvinceIdEffective());
        searchConditions.addElement("CityId").setText(request.getCityIdEffective());
        searchConditions.addElement("HotelId").setText(request.getHotelIdEffective());
        searchConditions.addElement("Lang").setText("GB");

        searchConditions.addElement("Currency").setText(stringValueOf(request.getCurrency()));
        searchConditions.addElement("RatePlanOnly").setText(stringValueOf(request.isRatePlanOnly()));

        Element stayDateRange = searchConditions.addElement("StayDateRange");
        stayDateRange.addElement("CheckIn").addText(DateUtil.format(request.getCheckIn(), "yyyy/MM/dd"));
        stayDateRange.addElement("CheckOut").addText(DateUtil.format(request.getCheckOut(), "yyyy/MM/dd"));

        MessageElement response =  doRequest(document.asXML());
        return HotelRoomResponseResolve.resolve(response);
    }


    /**
     * 发送请求值
     */
    private static MessageElement doRequest(String xmlStr) throws Exception {
        //实例化访问对象
        Service service = new Service();
        //实例化调用对象
        Call call = (Call) service.createCall();
        //在调用对象中添加WebService地址
        call.setTargetEndpointAddress(new java.net.URL(URL));
        call.setOperationName(new QName(TARGET_NAMESPACE, OPTION_NAME));

        Object[] paramValues = new Object[1];
        call.addParameter(new QName(TARGET_NAMESPACE, XML_REQUEST), XMLType.SOAP_STRING, ParameterMode.IN);
        paramValues[0] = xmlStr;

        //设置返回值格式（字符串或者组装对象）
        call.setReturnType(XMLType.SOAP_DOCUMENT);
        //是否是SOAPAction这里需要看WebService是否要求如下格式，如果没有要求可以不添加此设置
        call.setUseSOAPAction(true);
        //如果前面要求是SOAPAction的话需要添加下面设置，指定访问那个命名空间上的那个方法
        call.setSOAPActionURI(SOAP_ACTION);
        //在调用对象中添加WebService对应的命名空间，以及将要调用的函数名
        call.invoke(new Object[]{xmlStr});
        Message msg = call.getResponseMessage();
        SOAPBody soapBody =  msg.getSOAPBody();
        Node node = soapBody.getFirstChild();
        if (node.getNodeName().equals("GetXmlDataResponse")){
            Node result = node.getFirstChild();
            if (result.getNodeName().equals("GetXmlDataResult")){
                Node cn = result.getFirstChild();
                if (cn.getNodeName().equals("CNResponse")){
                    return (MessageElement)cn;
                }
            }
        }
        return null;
    }



    private static Document getDefaultHandlerData(LtBaseRequest baseRequest){
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("CNRequest");

        Element actionName = root.addElement("ActionName");
        actionName.setText(baseRequest.getActionName());

        Element identityInfo = root.addElement("IdentityInfo");
        //测试环境1
        identityInfo.addElement("AppId").setText("1");
        identityInfo.addElement("SecurityKey").setText("369b469c-51b2-43cd-9677-934ca17f2651");
        identityInfo.addElement("UserName").setText("EN000001");
        identityInfo.addElement("PassWord").setText("E10ADC3949BA59ABBE56E057F20F883E");
        identityInfo.addElement("Signature").setText("RU4wMDAwMDFFMTBBREMzOTQ5QkE1OUFCQkU1NkUwNTdGMjBGODgzRTM2OWI0NjljLTUxYjItNDNjZC05Njc3LTkzNGNhMTdmMjY1MQ==");

        //正式环境s
//        identityInfo.addElement("AppId").setText("707");
//        identityInfo.addElement("SecurityKey").setText("d34ff5c2-6aa0-481d-a4df-72f6945b0005");
//        identityInfo.addElement("UserName").setText("BG100011");
//        identityInfo.addElement("PassWord").setText("e10adc3949ba59abbe56e057f20f883e");
//        identityInfo.addElement("Signature").setText("QkcxMDAwMTFFMTBBREMzOTQ5QkE1OUFCQkU1NkUwNTdGMjBGODgzRWQzNGZmNWMyLTZhYTAtNDgxZC1hNGRmLTcyZjY5NDViMDAwNQ==");

        Element scrollingInfo = root.addElement("ScrollingInfo");
        //50:默认;40;全部查询;30:分页查询
        scrollingInfo.addElement("DisplayReq").setText(String.valueOf(baseRequest.getDisplayReq()));
        //DisplayReq 为 50 时,返回的记录数: DisplayReq
        //为 40 时,改节点无效; DisplayReq 为 30 时,每页
        //记录数
        String pageItems = baseRequest.getPageItems() == null ? "" : String.valueOf(baseRequest.getPageItems());
        scrollingInfo.addElement("PageItems").setText(pageItems);
        //DisplayReq为30时,分页编号; DisplayReq为40
        //或 50 时无效
        String pageNo = baseRequest.getPageNo() == null ? "" : String.valueOf(baseRequest.getPageNo());
        scrollingInfo.addElement("PageNo").setText(pageNo);
        return doc;
    }



    private static String stringValueOf(Object data){
        if (data == null){
            return "";
        }
        return String.valueOf(data);
    }

    public static void main(String[] args) throws Exception {
//        LtHotelResult result = getHotelSearch(new LtHotelRequest());
//        System.out.println(JSON.toJSONString(result));
//
        LtHotelRoomRequest roomRequest = new LtHotelRoomRequest();
        roomRequest.setHotelId("1574");
        roomRequest.setCheckIn(new Date());
        roomRequest.setCheckOut(DateUtil.offsetDay(new Date(),1));
        LtHotelResult hotelInfoPrice = getHotelInfoPrice(roomRequest);
        System.out.println(JSON.toJSONString(hotelInfoPrice));

    }
}
