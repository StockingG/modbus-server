package com.meller.modbusserver.entity.func.request;


import com.meller.modbusserver.entity.func.AbstractFunction;

/**
 * 读寄存器的请求
 *
 * @author chenleijun
 */
public class ReadHoldingRegistersRequest extends AbstractFunction {

    /**
     * 起始地址 = 0x0000 to 0xFFFF
     * 输入寄存器的数量 = 1 - 125
     */
    public ReadHoldingRegistersRequest() {
        super(READ_HOLDING_REGISTERS);
    }

    public ReadHoldingRegistersRequest(int startingAddress, int quantityOfInputRegisters) {
        super(READ_HOLDING_REGISTERS, startingAddress, quantityOfInputRegisters);
    }

    public int getStartingAddress() {
        return address;
    }

    public int getQuantityOfInputRegisters() {
        return value;
    }

    @Override
    public String toString() {
        return "ReadHoldingRegistersRequest{" + "startingAddress=" + address + ", quantityOfInputRegisters=" + value + '}';
    }
}
