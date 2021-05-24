package com.rubber.project.handler.impl.origin.lt.resolve;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.axis.message.MessageElement;

import javax.xml.namespace.QName;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/4/6
 */
public class BaseResolve {


    protected static Map<String,String> resolveMessageInfo(MessageElement element){
        HashMap<String,String> map = new HashMap<>();
        Iterator iterable = element.getChildElements();
        if (CollUtil.isNotEmpty(iterable)){
            while (iterable.hasNext()){
                MessageElement i = (MessageElement)iterable.next();
                map.put(i.getName(),i.getValue());
            }
        }
        return map;
    }


    protected static void resoleDepthJson(MessageElement element, JSONObject jsonObject){
        Iterator  child =  element.getChildElements();
        if (child.hasNext()){
            JSONObject childJson = new JSONObject();
            while (child.hasNext()) {
                MessageElement childElement = (MessageElement) child.next();
                resoleDepthJson(childElement,childJson);
            }
            jsonObject.put(element.getName(),childJson);
        }else if (StrUtil.isNotEmpty(element.getValue())){
            jsonObject.put(element.getName(),element.getValue());
        }
    }


    protected <T> T doResolveLandmarkBean(T object,Class<?> clz,MessageElement messageElement){
        try{
            Field[] fields = clz.getDeclaredFields();
            for (Field field:fields){
                if (field.getGenericType().toString().startsWith("java.util.List")){
                    continue;
                }
                String name = field.getName();
                name = StrUtil.upperFirst(name);
                MessageElement childElement = messageElement.getChildElement(new QName(name));
                if (childElement != null){
                    Method method = clz.getDeclaredMethod("set"+name, String.class);
                    method.invoke(object,childElement.getValue());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }

}
