package com.rubber.project.lt.resolve;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.rubber.project.lt.response.LtCityInfoResult;
import com.rubber.project.lt.response.bean.CityBean;
import com.rubber.project.lt.response.bean.CountryBean;
import com.rubber.project.lt.response.bean.ProvinceBean;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.*;

/**
 * @author luffyu
 * Created on 2021/4/5
 */
public class CityInfoResolve {



    public static LtCityInfoResult resolve(Document doc){
        LtCityInfoResult result = new LtCityInfoResult();
        Element element = doc.getRootElement();
        Iterator iterator = element.elementIterator();
        if (CollUtil.isNotEmpty(iterator)){
            while (iterator.hasNext()){
                Element childElement = (Element) iterator.next();
                if ("ActionName".equals(childElement.getName())){
                    result.setActionName(childElement.getStringValue());
                }else if ("MessageInfo".equals(childElement.getName())){
                    Map<String,String> map = resolveMessageInfo(childElement);
                    result.setCode(map.get("Code"));
                    result.setDescription(map.get("Description"));
                }else if ("Data".equals(childElement.getName())){
                    result.setCountryList(handlerResolveCountry(childElement));
                }
            }
        }
        return result;
    }


    public static List<CountryBean> handlerResolveCountry(Element element){
        List<CountryBean> countryBeans = new ArrayList<>();
        List countrysList=element.elements("Countrys");
        if (CollUtil.isNotEmpty(countrysList)){
            Element countrys = (Element)countrysList.get(0);
            List<Element> countryList  = countrys.elements("Country");
            for (Element country:countryList){
                //当前国家
                CountryBean countryBean = JSONObject.parseObject(createElementAtt(country).toJSONString(),CountryBean.class);
                List<ProvinceBean> provinceBeans = new ArrayList<>();

                List<Element> provinceList  = country.elements("Province");
                if (CollUtil.isNotEmpty(provinceList)) {
                    for (Element province : provinceList) {
                        ProvinceBean provinceBean = JSONObject.parseObject(createElementAtt(province).toJSONString(), ProvinceBean.class);
                        List<Element> cityList = province.elements("City");
                        List<CityBean> cityBeans = new ArrayList<>();
                        if (CollUtil.isNotEmpty(cityList)) {
                            cityList.forEach(i -> {
                                CityBean cityBean = JSONObject.parseObject(createElementAtt(i).toJSONString(), CityBean.class);
                                cityBeans.add(cityBean);
                            });
                        }
                        provinceBean.setCityList(cityBeans);
                        provinceBeans.add(provinceBean);
                    }
                }
                countryBean.setProvinceList(provinceBeans);
                countryBeans.add(countryBean);
            }
        }
        return countryBeans;
    }


    private static JSONObject createElementAtt(Element country){
        List data = country.attributes();
        JSONObject result = new JSONObject();
        if (CollUtil.isNotEmpty(data)){
            data.forEach(i->{
                Attribute attribute = (Attribute)i;
                result.put(toLowerFirst(attribute.getName()),attribute.getValue());
            });
        }
        return result;
    }


    private static Map<String,String> resolveMessageInfo(Element element){
        HashMap<String,String> map = new HashMap<>();
        Iterator iterable = element.elementIterator();
        if (CollUtil.isNotEmpty(iterable)){
            while (iterable.hasNext()){
                Element i = (Element)iterable.next();
                map.put(i.getName(),i.getTextTrim());
            }
        }
        return map;
    }


    public static String toLowerFirst(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();

        }
    }
}
