package com.meller.modbusserver.utils;

/**
 * @author chenleijun
 * @Date 2018/6/12
 */
public class IntUtils {

    public static String byteToUnsignedHex(int i) {
        String hex = Integer.toHexString(i & 0xffff);
        while(hex.length() < 4){
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 返回 hex string
     * @param arr
     * @return String
     */
    public static String intArrToHex(int[] arr) {
        StringBuilder builder = new StringBuilder(arr.length * 4);
        for (int b : arr) {
            builder.append(byteToUnsignedHex(b));
        }
        return builder.toString();
    }

    public static long getUnSign32(int i1, int i2){
        return (i1<<16) + i2;
    }

    public static int getSign32(int i1, int i2){
        return (i1<<16) + i2;
    }

}
