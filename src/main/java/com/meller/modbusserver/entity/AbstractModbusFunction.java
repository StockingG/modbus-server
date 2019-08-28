package com.meller.modbusserver.entity;

import com.meller.modbusserver.config.MobsConstants;
import io.netty.buffer.ByteBuf;

/**
 * modbus 基本方法
 *
 * @author chenleijun
 */
public abstract class AbstractModbusFunction {

    /**
     * 读寄存器
     */
    public static final short READ_HOLDING_REGISTERS = 0x03;

    private final short functionCode;

    public AbstractModbusFunction(short functionCode) {
        this.functionCode = functionCode;
    }

    public short getFunctionCode() {
        return functionCode;
    }

    public static boolean isError(short functionCode) {
        return functionCode - MobsConstants.ERROR_OFFSET >= 0;
    }

    /**
     * 计算长度
     *
     * @return 长度
     */
    public abstract int calculateLength();

    /**
     * 编码
     *
     * @return ByteBuf 发送的二进制码
     */
    public abstract ByteBuf encode();

    /**
     * 解码
     *
     * @param data
     */
    public abstract void decode(ByteBuf data);
}
