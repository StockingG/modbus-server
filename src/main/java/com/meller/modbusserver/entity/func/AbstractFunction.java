package com.meller.modbusserver.entity.func;

import com.meller.modbusserver.entity.AbstractModbusFunction;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author chenleijun
 * @Date 2018/6/1
 */
public class AbstractFunction extends AbstractModbusFunction {
    /**
     * 寄存器地址
     */
    protected int address;
    /**
     * 寄存器数目
     */
    protected int value;

    public AbstractFunction(short functionCode) {
        super(functionCode);
    }

    public AbstractFunction(short functionCode, int address, int quantity) {
        super(functionCode);

        this.address = address;
        this.value = quantity;
    }

    @Override
    public int calculateLength() {
        //Unit Identifier + function code + address + quantity
        return 1 + 1 + 2 + 2;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer(calculateLength());
        buf.writeByte(getFunctionCode());
        buf.writeShort(address);
        buf.writeShort(value);

        return buf;
    }

    @Override
    public void decode(ByteBuf data) {
        address = data.readUnsignedShort();
        value = data.readUnsignedShort();
    }
}
