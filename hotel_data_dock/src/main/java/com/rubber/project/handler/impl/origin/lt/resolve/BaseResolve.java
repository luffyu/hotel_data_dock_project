package com.rubber.project.handler.impl.origin.lt.resolve;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.axis.message.MessageElement;

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

}
