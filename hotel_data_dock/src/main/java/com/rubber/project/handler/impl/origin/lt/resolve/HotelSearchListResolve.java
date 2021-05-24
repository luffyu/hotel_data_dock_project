package com.rubber.project.handler.impl.origin.lt.resolve;

import com.rubber.project.handler.impl.origin.lt.response.LtHotelSearchListResponse;
import com.rubber.project.handler.impl.origin.lt.response.bean.HotelSearchListBean;
import com.rubber.project.handler.impl.origin.lt.response.bean.ImageBean;
import com.rubber.project.handler.impl.origin.lt.response.bean.LandmarkBean;
import com.rubber.project.handler.impl.origin.lt.response.bean.ServiceShBean;
import org.apache.axis.message.MessageElement;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author luffyu
 * Created on 2021/5/24
 */
public class HotelSearchListResolve extends BaseResolve {



    public LtHotelSearchListResponse resolve(MessageElement messageElement){
        LtHotelSearchListResponse result = new LtHotelSearchListResponse();
        MessageElement actionName = messageElement.getChildElement(new QName("ActionName"));
        result.setActionName(actionName.getValue());

        MessageElement messageInfo = messageElement.getChildElement(new QName("MessageInfo"));
        result.setCode(messageInfo.getChildElement(new QName("Code")).getValue());
        result.setDescription(messageInfo.getChildElement(new QName("Description")).getValue());

        MessageElement data = messageElement.getChildElement(new QName("Data"));
        MessageElement hotelsElement = data.getChildElement(new QName("Hotels"));
        Iterator<MessageElement> hotel = hotelsElement.getChildElements(new QName("Hotel"));
        List<HotelSearchListBean> hotelSearchListBeans = new ArrayList<>();
        while (hotel.hasNext()){
            MessageElement element = hotel.next();
            HotelSearchListBean hotelSearchListBean = new HotelSearchListBean();
            hotelSearchListBean = doResolveLandmarkBean(hotelSearchListBean, HotelSearchListBean.class, element);

            MessageElement images = element.getChildElement(new QName("Images"));
            if (images == null){
                continue;
            }
            Iterator<MessageElement> imageElements = images.getChildElements(new QName("Image"));
            List<ImageBean> imageBeans= new ArrayList<>();
            while (imageElements.hasNext()){
                MessageElement elementNode  = imageElements.next();
                ImageBean bean = new ImageBean();
                bean = doResolveLandmarkBean(bean, ImageBean.class, elementNode);
                imageBeans.add(bean);
            }
            hotelSearchListBean.setImageBeans(imageBeans);


            MessageElement landmark = element.getChildElement(new QName("Landmarks"));
            if (landmark == null){
                continue;
            }
            Iterator<MessageElement> landmarkElements = landmark.getChildElements(new QName("Landmark"));
            List<LandmarkBean> landmarkBeans= new ArrayList<>();
            while (landmarkElements.hasNext()){
                MessageElement elementNode  = landmarkElements.next();
                LandmarkBean bean = new LandmarkBean();
                bean = doResolveLandmarkBean(bean, LandmarkBean.class, elementNode);
                landmarkBeans.add(bean);
            }
            hotelSearchListBean.setLandmarks(landmarkBeans);

            MessageElement services = element.getChildElement(new QName("Services"));
            if (services == null){
                continue;
            }
            Iterator<MessageElement> serviceElements = services.getChildElements(new QName("Service"));
            List<ServiceShBean> serviceBeans= new ArrayList<>();
            while (serviceElements.hasNext()){
                MessageElement elementNode  = serviceElements.next();
                ServiceShBean bean = new ServiceShBean();
                bean = doResolveLandmarkBean(bean, ServiceShBean.class, elementNode);
                serviceBeans.add(bean);
            }
            hotelSearchListBean.setServiceShBeans(serviceBeans);



            hotelSearchListBeans.add(hotelSearchListBean);
        }
        result.setHotelSearchListBean(hotelSearchListBeans);
        return result;
    }



//    private HotelSearchListBean doResolveSearchListBean(MessageElement messageElement){
//        HotelSearchListBean hotelSearchListBean = new HotelSearchListBean();
//        try{
//            Class<?> clz = hotelSearchListBean.getClass();
//            Field[] fields = hotelSearchListBean.getClass().getDeclaredFields();
//            for (Field field:fields){
//                if (field.getGenericType().toString().startsWith("java.util.List")){
//                    continue;
//                }
//                String name = field.getName();
//                name = StrUtil.upperFirst(name);
//                MessageElement childElement = messageElement.getChildElement(new QName(name));
//                if (childElement != null){
//                    Method method = clz.getDeclaredMethod("set"+name, String.class);
//                    method.invoke(hotelSearchListBean,childElement.getValue());
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return hotelSearchListBean;
//    }



 }
