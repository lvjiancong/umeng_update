//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dou361.jjdxm_update;


import com.dou361.base64.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Sign {
    private static final String CONTENT_CHARSET = "UTF-8";
    private static final String HMAC_ALGORITHM = "HmacSHA1";

    public Sign() {
    }

    public static String sign(String signStr, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String sig = null;
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(secretKey);
        byte[] hash = mac.doFinal(signStr.getBytes("UTF-8"));
        BASE64Encoder mm = new BASE64Encoder();
        sig = new String(mm.encode(hash));
        return sig;
    }

    public static String makeSignPlainText(TreeMap<String, Object> requestParams, String requestMethod) {
        String retStr = "";
        retStr = retStr + buildParamStr(requestParams, requestMethod);
        return retStr;
    }

    protected static String buildParamStr(TreeMap<String, Object> requestParams, String requestMethod) {
        String retStr = "";
        Iterator var4 = requestParams.keySet().iterator();

        while (true) {
            String key;
            do {
                do {
                    if (!var4.hasNext()) {
                        return retStr;
                    }

                    key = (String) var4.next();
                } while (key.equals("Debug"));
            }
            while (requestMethod == "POST" && requestParams.get(key).toString().substring(0, 1).equals("@"));

            if (retStr.length() == 0) {
                retStr = retStr + '?';
            } else {
                retStr = retStr + '&';
            }

            retStr = retStr + key.replace("_", ".") + '=' + requestParams.get(key).toString();
        }
    }
}
