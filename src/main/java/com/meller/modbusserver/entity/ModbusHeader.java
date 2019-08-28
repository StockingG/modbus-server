package com.meller.modbusserver.entity;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * modbus 头部
 *
 * @author chenleijun
 */
public class ModbusHeader {

    /**
     * 传输标识符
     */
    private final int transactionIdentifier;

    /**
     * 协议类型
     */
    private final int protocolIdentifier;

    /**
     * 数据长度
     */
    private final int length;

    /**
     * 逻辑设备Id
     */
    private final short unitIdentifier;

    public ModbusHeader(int transactionIdentifier, int protocolIdentifier, int pduLength, short unitIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
        this.protocolIdentifier = protocolIdentifier;
        this.length = pduLength;
        this.unitIdentifier = unitIdentifier;
    }

    public int getLength() {
        return length;
    }

    public int getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public int getTransactionIdentifier() {
        return transactionIdentifier;
    }

    public short getUnitIdentifier() {
        return unitIdentifier;
    }

    /**
     * 解码
     *
     * @param buffer
     * @return
     */
    public static ModbusHeader decode(ByteBuf buffer) {
        return new ModbusHeader(buffer.readUnsignedShort(),
                buffer.readUnsignedShort(),
                buffer.readUnsignedShort(),
                buffer.readUnsignedByte());
    }

    /**
     * 编码
     *
     * @return
     */
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();

        buf.writeShort(transactionIdentifier);
        buf.writeShort(protocolIdentifier);
        buf.writeShort(length);
        buf.writeByte(unitIdentifier);

        return buf;
    }

    @Override
    public String toString() {
        return "ModbusHeader{" + "transactionIdentifier=" + transactionIdentifier + ", protocolIdentifier=" + protocolIdentifier + ", length=" + length + ", unitIdentifier=" + unitIdentifier + '}';
    }
}
