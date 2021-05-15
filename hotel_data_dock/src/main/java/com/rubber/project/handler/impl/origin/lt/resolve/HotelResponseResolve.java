package com.rubber.project.handler.impl.origin.lt.resolve;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rubber.project.handler.impl.origin.lt.response.LtHotelResponse;
import com.rubber.project.handler.impl.origin.lt.response.LtHotelResult;
import org.apache.axis.message.MessageElement;
import org.w3c.dom.Node;

import java.util.*;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
public class HotelResponseResolve extends BaseResolve {


    public static LtHotelResult resolve(MessageElement messageElement){
        LtHotelResult result = new LtHotelResult();
        Iterator iterable = messageElement.getChildElements();
        while (iterable.hasNext()){
            MessageElement element = (MessageElement)iterable.next();
            if ("ActionName".equals(element.getName())){
                result.setActionName(element.getValue());
            }else if ("MessageInfo".equals(element.getName())){
                Map<String,String> map = resolveMessageInfo(element);
                result.setCode(map.get("Code"));
                result.setDescription(map.get("Description"));
            }else if ("data".equals(element.getName())){
                JSONArray data = resoleData(element);
                result.setResponseList(JSON.parseArray(data.toJSONString(), LtHotelResponse.class));
            }
        }
        return result;
    }


    private static JSONArray resoleData(MessageElement element){
        JSONArray hotelData = new JSONArray();
        Node node = element.getFirstChild();
        if (node != null){
            MessageElement hotels = (MessageElement) node;
            Iterator  child =  hotels.getChildElements();
            if (CollUtil.isNotEmpty(child)){
                while (child.hasNext()){
                    MessageElement childElement = (MessageElement) child.next();
                    if ("hotel".equals(childElement.getName())){
                        JSONObject jsonObject = new JSONObject();
                        resoleDepthJson(childElement,jsonObject);
                        hotelData.add(jsonObject);
                    }
                }
            }
        }
        return hotelData ;
    }

}
