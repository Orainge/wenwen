package com.orainge.wenwen.shiro.util;

import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommonUtil {

    /**
     * 生成token
     *
     * @return String[]{encodeEmail, originalToken, encodeToken}
     */
    public static String getToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String[] getToken(String email) {
        String encodeEmail = DigestUtils.md5DigestAsHex(email.getBytes());
        String originalToken = getToken();
        String encodeToken = encodeEmail + originalToken; //TODO 进行位移加密

        return new String[]{encodeEmail, originalToken, encodeToken};
    }

    /**
     * 验证 token 格式是否有效，不检查是否存在于 redis
     *
     * @return String[]{encodeEmail, token}
     */
    public static String[] verifyToken(String encodeToken) {
        if (encodeToken.length() == 64) {
            String encodeEmail = encodeToken.substring(0, 32);
            String originalToken = encodeToken.substring(32, 64);
            return new String[]{encodeEmail, originalToken};
        } else {
            //token无效
            return null;
        }
    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    private static String convertMD5(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }

    public static String entrypt(String password) {
        String result = null;
        // TODO 实现加密过程
        return result;
    }

    public static String decrypt(String password) {
        String result = null;
        // TODO 实现解密过程
        result = password;
        return result;

    }
}
