package com.rubber.project.handler.impl.origin.lt.http;

import com.rubber.project.handler.impl.origin.lt.resolve.CityInfoResolve;
import com.rubber.project.handler.impl.origin.lt.response.LtCityInfoResult;
import org.dom4j.DocumentHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 静态资源的请求
 * @author luffyu
 * Created on 2021/4/5
 */
public class StaticHttpUtils {

    //private static final String url = "http://202.104.101.32:8077/StaticInfoQuery.aspx?auth=RU4wMDAwMDFFMTBBREMzOTQ5QkE1OUFCQkU1NkUwNTdGMjBGODgzRTM2OWI0NjljLTUxYjItNDNjZC05Njc3LTkzNGNhMTdmMjY1MVJVNHdNREF3TURGRk1UQkJSRU16T1RRNVFrRTFPVUZDUWtVMU5rVXdOVGRHTWpCR09EZ3pSVE0yT1dJME5qbGpMVFV4WWpJdE5ETmpaQzA1TmpjM0xUa3pOR05oTVRkbU1qWTFNUT09&ActionName=";

    private static final String url = "http://lvtianxia.dingfangyi.com/RESTSERVER.asmx?auth=QkcxMDAwMTFFMTBBREMzOTQ5QkE1OUFCQkU1NkUwNTdGMjBGODgzRWQzNGZmNWMyLTZhYTAtNDgxZC1hNGRmLTcyZjY5NDViMDAwNQ==&ActionName=";


    /**
     * 获取城市的相关信息
     */
    public static LtCityInfoResult getStaticCityInfo(){
        org.dom4j.Document  document = getStaticXmlData("CityQuery");
        return CityInfoResolve.resolve(document);
    }



    /**
     * 获取静态的资源方法
     */
    private static org.dom4j.Document getStaticXmlData(String actionName) {
        try {
            String urlStr = url + actionName;
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                stringBuilder.append(line);
            }
            String response =  stringBuilder.toString();
            Document doc = Jsoup.parse(response);
            Element element = doc.body().getElementById("xmlPrview");
            return DocumentHelper.parseText(element.text());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        getStaticCityInfo();
    }
}
