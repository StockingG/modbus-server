package com.meller.modbusserver.utils;

import com.meller.modbusserver.config.MobsConstants;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author chenleijun
 * @Date 2018/6/12
 */
public class CommonUtils {

    private static int lastTransactionIdentifier = 0;

    /**
     * 计算传输标识符累计
     *
     * @return
     */
    public static synchronized int calculateTransactionIdentifier() {
        if (lastTransactionIdentifier < MobsConstants.TRANSACTION_IDENTIFIER_MAX) {
            lastTransactionIdentifier++;
        } else {
            lastTransactionIdentifier = 1;
        }
        return lastTransactionIdentifier;
    }

    /**
     * 生成uuid
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 计算 double 乘法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
}
