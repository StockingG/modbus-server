package com.meller.modbusserver.entity;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * modbus 对象
 *
 * @author chenleijun
 */
public class ModbusFrame {

    private final ModbusHeader header;
    private final AbstractModbusFunction function;

    public ModbusFrame(ModbusHeader header, AbstractModbusFunction function) {
        this.header = header;
        this.function = function;
    }

    public ModbusHeader getHeader() {
        return header;
    }

    public AbstractModbusFunction getFunction() {
        return function;
    }

    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();

        buf.writeBytes(header.encode());
        buf.writeBytes(function.encode());

        return buf;
    }

    @Override
    public String toString() {
        return "ModbusFrame{" + "header=" + header + ", function=" + function + '}';
    }
}
