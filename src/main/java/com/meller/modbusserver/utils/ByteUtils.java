package com.meller.modbusserver.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;

/**
 * @author chenleijun
 * @Date 2018/6/12
 */
public class ByteUtils {

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static byte[] getByteArray(int i){
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(i);
        return b.array();
    }

    /**
     * String
     */
    public static String byteArray2Str(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String s = new String(byteArray);
        if(s.contains("\u0000")){
            s = s.split("\u0000")[0];
        }
        return s;
    }

    /**
     * 将 byte 转换为一个长度为 8 的 byte 数组，数组每个值代表 bit
     */
    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    /**
     * 将 byte[2] bytes 转换成 bit[]数组
     */
    public static byte[] getWarnArray(byte[] bytes) {

        byte[] array1 = getBooleanArray(bytes[0]);
        byte[] array2 = getBooleanArray(bytes[1]);
        byte[] array = ArrayUtils.addAll(array1, array2);
        ArrayUtils.reverse(array);
        return array;
    }
}
