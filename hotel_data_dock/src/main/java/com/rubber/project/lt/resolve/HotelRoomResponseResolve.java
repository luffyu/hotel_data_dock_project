package com.rubber.project.lt.resolve;

import cn.hutool.core.collection.CollUtil;
import com.rubber.project.lt.response.LtHotelResponse;
import com.rubber.project.lt.response.LtHotelResult;
import com.rubber.project.lt.response.LtRoomPlanResponse;
import com.rubber.project.lt.response.LtRoomResponse;
import org.apache.axis.message.MessageElement;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luffyu
 * Created on 2021/4/6
 */
public class HotelRoomResponseResolve extends BaseResolve {

    public static LtHotelResult resolve(MessageElement messageElement){
        LtHotelResult result = new LtHotelResult();
        MessageElement actionName = messageElement.getChildElement(new QName("ActionName"));
        result.setActionName(actionName.getValue());

        MessageElement messageInfo = messageElement.getChildElement(new QName("MessageInfo"));
        result.setCode(messageInfo.getChildElement(new QName("Code")).getValue());
        result.setDescription(messageInfo.getChildElement(new QName("Description")).getValue());

        MessageElement data = messageElement.getChildElement(new QName("Data"));
        MessageElement hotelsElement = data.getChildElement(new QName("Hotels"));
        Iterator<MessageElement> hotelArray = hotelsElement.getChildElements(new QName("Hotel"));

        List<LtHotelResponse> ltHotelResponses = new ArrayList<>();
        while (hotelArray.hasNext()){
            LtHotelResponse response = resoleHotelData(hotelArray.next());
            ltHotelResponses.add(response);
        }
        result.setResponseList(ltHotelResponses);
        return result;
    }


    private static LtHotelResponse resoleHotelData(MessageElement hotel){
        LtHotelResponse hotelResponse = new LtHotelResponse();
        hotelResponse.setHotelId(hotel.getChildElement(new QName("HotelId")).getValue());
        hotelResponse.setHotelName(hotel.getChildElement(new QName("HotelName")).getValue());

        List<LtRoomResponse> roomResponseArrayList = new ArrayList<>();
        MessageElement rooms =  hotel.getChildElement(new QName("Rooms"));
        Iterator<MessageElement> roomArray =  rooms.getChildElements(new QName("Room"));
        while (roomArray.hasNext()){
            LtRoomResponse roomResponse = resoleRoomData(roomArray.next());
            roomResponseArrayList.add(roomResponse);
        }
        hotelResponse.setRoomList(roomResponseArrayList);
        return hotelResponse;
    }


    private static LtRoomResponse resoleRoomData(MessageElement room){
        LtRoomResponse ltRoomResponse = new LtRoomResponse();
        ltRoomResponse.setRoomId(room.getChildElement(new QName("RoomId")).getValue());
        ltRoomResponse.setRoomName(room.getChildElement(new QName("RoomName")).getValue());

        List<LtRoomPlanResponse> rateResponseList = new ArrayList<>();
        MessageElement rates = room.getChildElement(new QName("Rates"));
        Iterator<MessageElement> rateArray = rates.getChildElements(new QName("Rate"));
        while (rateArray.hasNext()){
            List<LtRoomPlanResponse> rateResponse = resolePlanRatesData(rateArray.next());
            rateResponseList.addAll(rateResponse);
        }

        List<LtRoomPlanResponse> planResponseList = new ArrayList<>();
        MessageElement ratePlans = room.getChildElement(new QName("RatePlans"));
        Iterator<MessageElement> ratePlanArray = ratePlans.getChildElements(new QName("RatePlan"));
        while (ratePlanArray.hasNext()){
            LtRoomPlanResponse ratePlansResponse = resolePlanData(ratePlanArray.next());
            planResponseList.add(ratePlansResponse);
        }


        if(CollUtil.isNotEmpty(planResponseList)){
            Map<String,LtRoomPlanResponse> map = planResponseList.stream().collect(Collectors.toMap(LtRoomPlanResponse::getRatePlanId, i->i));
            rateResponseList.forEach(i->{
                LtRoomPlanResponse planResponse = map.get(i.getRatePlanId());
                if (planResponse != null){
                    i.setBreakfast(planResponse.getBreakfast());
                    i.setBreakfastType(planResponse.getBreakfastType());
                }
            });
        }


        ltRoomResponse.setRoomPlans(rateResponseList);
        return ltRoomResponse;
    }


    private static LtRoomPlanResponse resolePlanData(MessageElement plan){
        LtRoomPlanResponse ltRoomPlanResponse = new LtRoomPlanResponse();
        ltRoomPlanResponse.setRatePlanId(plan.getChildElement(new QName("RatePlanId")).getValue());
        ltRoomPlanResponse.setRatePlanName(plan.getChildElement(new QName("RatePlanName")).getValue());
        ltRoomPlanResponse.setBreakfast(plan.getChildElement(new QName("Breakfast")).getValue());
        ltRoomPlanResponse.setBreakfastType(plan.getChildElement(new QName("BreakfastType")).getValue());
        return ltRoomPlanResponse;
    }

    private static List<LtRoomPlanResponse> resolePlanRatesData(MessageElement rate){
        List<LtRoomPlanResponse> planResponses = new ArrayList<>();
        MessageElement priceAndStatus = rate.getChildElement(new QName("PriceAndStatus"));
        Iterator<MessageElement> priceAndStatu = priceAndStatus.getChildElements(new QName("PriceAndStatu"));
        while (priceAndStatu.hasNext()){
            MessageElement rateNode = priceAndStatu.next();
            LtRoomPlanResponse ltRoomPlanResponse = new LtRoomPlanResponse();
            ltRoomPlanResponse.setRatePlanId(rate.getChildElement(new QName("RatePlanId")).getValue());
            ltRoomPlanResponse.setRatePlanName(rate.getChildElement(new QName("RatePlanName")).getValue());
            ltRoomPlanResponse.setRateId(rateNode.getChildElement(new QName("RateId")).getValue());
            ltRoomPlanResponse.setData(rateNode.getChildElement(new QName("Date")).getValue());
            ltRoomPlanResponse.setPrice(rateNode.getChildElement(new QName("Price")).getValue());
            ltRoomPlanResponse.setCurrency(rateNode.getChildElement(new QName("Currency")).getValue());
            ltRoomPlanResponse.setCount(rateNode.getChildElement(new QName("Count")).getValue());
            ltRoomPlanResponse.setStatu(rateNode.getChildElement(new QName("Statu")).getValue());
            planResponses.add(ltRoomPlanResponse);
        }
        return planResponses;
    }


}
