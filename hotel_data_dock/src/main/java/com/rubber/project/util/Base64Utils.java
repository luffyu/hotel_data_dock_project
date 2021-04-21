package com.rubber.project.util;

import cn.hutool.core.util.StrUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author luffyu
 * Created on 2021/4/4
 */
public class Base64Utils {




    /**
     * BASE64解密
     * @throws Exception
     */
    public static String decryptStr(String key) throws Exception {
        byte[] result = decrypt(key);
        return StrUtil.str(result,"GBK");
    }

    /**
     * BASE64解密
     * @throws Exception
     */
    public static byte[] decrypt(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }




    /**
     * BASE64加密
     */
    public static String encryptStr(String key) throws Exception {
        byte[] keyArray = StrUtil.bytes(key);
        return (new BASE64Encoder()).encodeBuffer(keyArray);
    }


    /**
     * BASE64加密
     */
    public static String encrypt(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

}
