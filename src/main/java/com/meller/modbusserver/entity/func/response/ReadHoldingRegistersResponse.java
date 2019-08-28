package com.meller.modbusserver.entity.func.response;

import com.meller.modbusserver.entity.AbstractModbusFunction;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


/**
 * 读寄存器的应答
 *
 * @author chenleijun
 * @Date 2018/6/1
 */
public class ReadHoldingRegistersResponse extends AbstractModbusFunction {

    private short byteCount;
    private int[] registers;

    public ReadHoldingRegistersResponse() {
        super(READ_HOLDING_REGISTERS);
    }

    public ReadHoldingRegistersResponse(int[] registers) {
        super(READ_HOLDING_REGISTERS);

        // 寄存器最大个数125
        if (registers.length > 250) {
            throw new IllegalArgumentException();
        }

        this.byteCount = (short) (registers.length);
        this.registers = registers;
    }

    public int[] getRegisters() {
        return registers;
    }

    public short getByteCount() {
        return byteCount;
    }

    @Override
    public int calculateLength() {
        return 1 + 1 + byteCount;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer(calculateLength());
        buf.writeByte(getFunctionCode());
        buf.writeByte(byteCount);

        for (int i = 0; i < registers.length; i++) {
            buf.writeShort(registers[i]);
        }

        return buf;
    }

    @Override
    public void decode(ByteBuf data) {
        byteCount = data.readUnsignedByte();

        registers = new int[byteCount / 2];
        for (int i = 0; i < registers.length; i++) {
            registers[i] = data.readUnsignedShort();
        }
    }

    @Override
    public String toString() {
        StringBuilder registersStr = new StringBuilder();
        registersStr.append("{");
        for (int i = 0; i < registers.length; i++) {
            registersStr.append("register_");
            registersStr.append(i);
            registersStr.append("=");
            registersStr.append(registers[i]);
            registersStr.append(", ");
        }
        registersStr.delete(registersStr.length() - 2, registersStr.length());
        registersStr.append("}");

        return "ReadHoldingRegistersResponse{" + "byteCount=" + byteCount + ", inputRegisters=" + registersStr + '}';
    }
}

