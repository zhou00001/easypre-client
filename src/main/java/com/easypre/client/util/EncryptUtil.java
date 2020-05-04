package com.easypre.client.util;


import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * 加密工具类
 * @author zhoudc
 * @since 1.0
 */
public class EncryptUtil {
    private static final HashFunction HASH_MURMUR_3_128_FUNCTION = Hashing.murmur3_128();
    /**
     * 加密字符串为Base64编码
     * @param str
     * @return
     */
    public static String encodeToBase64(String str){
        return encodeToBase64(str.getBytes(Charset.forName("UTF-8")));
    }
    public static String encodeToBase64(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 加密密码
     * @param pwd
     * @param salt
     * @return
     */
    public static String encryptPassword(String pwd,String salt){
        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            String result = byteArrayToHexString(md.digest((pwd + "{" + salt + "}").getBytes("UTF-8")));
            return result;
        }catch (Exception ex){
            return null;
        }
    }

    /**
     * 获取sha1散列值
     * @param str
     * @return
     */
    public static String sha1(String str){
        return DigestUtils.sha1Hex(str.getBytes());
    }

    /**
     * md5加密
     * @param str
     * @return
     */
    public static String md5(String str){
        return DigestUtils.md5Hex(str);
    }
    /**
     * hash算法
     * @param str
     * @return
     */
    public static long hashNoEncrypt(String str){
        return HASH_MURMUR_3_128_FUNCTION.hashString(str,Charset.forName("UTF-8")).asLong();
    }
    public static String hashNoEncryptToStr(String str){
        return StringUtil.radix10To64(hashNoEncrypt(str));
    }
    /**
     * 转换字节数组为16进制字串
     *
     * @param b
     *            字节数组
     * @return 16进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    private final static String[] HEX_DIGITS = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }
}
