package com.orainge.wenwen.shiro.util;

import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommonUtil {

    /**
     * 生成token
     */
    public static String[] getToken(String email) {
        String encodeEmail = DigestUtils.md5DigestAsHex(email.getBytes());
        String originalToken = UUID.randomUUID().toString().replace("-", "");
        char[] chars = (encodeEmail + originalToken).toCharArray();
        char[] charsNew = new char[64];

        for (int i = 0; i <= 7; i++) {
            charsNew[i] = chars[i + 56];
        }// 0-7 放入 56-63

        for (int i = 8; i <= 15; i++) {
            charsNew[i] = chars[i + 16];
        }// 8-15 放入 24-31

        for (int i = 16; i <= 23; i++) {
            charsNew[i] = chars[i + 24];
        }// 16-23 放入 40-47

        for (int i = 24; i <= 31; i++) {
            charsNew[i] = chars[i - 24];
        }// 24-31 放入 0-7

        for (int i = 32; i <= 39; i++) {
            charsNew[i] = chars[i + 16];
        }// 32-39 放入 48-55

        for (int i = 40; i <= 47; i++) {
            charsNew[i] = chars[i - 32];
        }// 40-47 放入 8-15

        for (int i = 48; i <= 55; i++) {
            charsNew[i] = chars[i - 16];
        }// 48-55 放入 32-39

        for (int i = 56; i <= 63; i++) {
            charsNew[i] = chars[i - 40];
        }// 56-63 放入 16-23

        //[8][4][6][1][7][2][5][3]
        String encodeToken = new String(charsNew);
        return new String[]{encodeEmail, originalToken, encodeToken};
    }

    /**
     * 验证 token 格式是否有效，不检查是否存在于 redis
     *
     * @return String[]{encodeEmail, token}
     */
    public static String[] verifyToken(String encodeToken) {
        if (encodeToken.length() == 64) {
            char[] chars = encodeToken.toCharArray();
            char[] charsNew = new char[64];

            for (int i = 0; i <= 7; i++) {
                charsNew[i] = chars[i + 24];
            }// 0-7 放入 24-31

            for (int i = 8; i <= 15; i++) {
                charsNew[i] = chars[i + 32];
            }// 8-15 放入 40-47

            for (int i = 16; i <= 23; i++) {
                charsNew[i] = chars[i + 40];
            }// 16-23 放入 56-63

            for (int i = 24; i <= 31; i++) {
                charsNew[i] = chars[i - 16];
            }// 24-31 放入 8-15

            for (int i = 32; i <= 39; i++) {
                charsNew[i] = chars[i + 16];
            }// 32-39 放入 48-55

            for (int i = 40; i <= 47; i++) {
                charsNew[i] = chars[i - 24];
            }// 40-47 放入 16-23

            for (int i = 48; i <= 55; i++) {
                charsNew[i] = chars[i - 16];
            }// 48-55 放入 32-39

            for (int i = 56; i <= 63; i++) {
                charsNew[i] = chars[i - 56];
            }// 56-63 放入 0-7

            String strs = new String(charsNew);
            String encodeEmail = strs.substring(0, 32);
            String originalToken = strs.substring(32, 64);
            return new String[]{encodeEmail, originalToken};
        } else {
            //token无效
            return null;
        }
    }
}
