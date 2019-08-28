package com.meller.modbusserver.config;


import com.meller.modbusserver.config.coon.Connection;
import io.netty.util.AttributeKey;

/**
 * MODBUS 的一些常量
 *
 * @author chenleijun
 */
public class MobsConstants {

    /**
     * 错误偏移
     */
    public static final int ERROR_OFFSET = 0x80;

    /**
     * affects memory usage of library
     */
    public static final int TRANSACTION_IDENTIFIER_MAX = 0xffff;

    /**
     * adu 最大长度
     */
    public static final int ADU_MAX_LENGTH = 260;

    /**
     * MBAP 长度
     */
    public static final int MBAP_LENGTH = 7;

    /**
     * 默认modbus server开放端口
     */
    public static final int DEFAULT_MODBUS_PORT = 502;

    /**
     * 默认协议标识符
     */
    public static final short DEFAULT_PROTOCOL_IDENTIFIER = 0;

    /**
     * 默认逻辑设备id
     */
    public static final short DEFAULT_UNIT_IDENTIFIER = 0;

    /**
     * connenction key
     */
    public static final AttributeKey<Connection> CONN_KEY = AttributeKey.valueOf("CONN_KEY");

    public static final String SERIALNUMBER = "serialNumber";
    public static final String BASICINFO = "basicInfo";
    public static final String EXTRAINFO = "extraInfo";
}
