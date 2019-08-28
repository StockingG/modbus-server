/*
 * Copyright 2012 modjn Project
 *
 * The modjn Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.meller.modbusserver.entity.func;

import com.meller.modbusserver.entity.AbstractModbusFunction;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;

/**
 * modbus 异常码列表 方法
 *
 * @author chenleijun
 */
public class ModbusError extends AbstractModbusFunction {
    /*
     * Modbus Exception Codes
     *
     * 01 ILLEGAL FUNCTION (非法功能)
     *
     * The function code received in the query is not an allowable action for
     * the server (or slave). This may be because the function code is only
     * applicable to newer devices, and was not implemented in the unit
     * selected. It could also indicate that the server (or slave) is in the
     * wrong state to process a request of this type, for example because it is
     * unconfigured and is being asked to return register values.
     *
     * 02 ILLEGAL DATA ADDRESS (非法数据地址)
     *
     * The data address received in the query is not an allowable address for
     * the server (or slave). More specifically, the combination of reference
     * number and transfer length is invalid. For a web with 100
     * registers, the PDU addresses the first register as 0, and the last one as
     * 99. If a request is submitted with a starting register address of 96 and
     * a quantity of registers of 4, then this request will successfully operate
     * (address-wise at least) on registers 96, 97, 98, 99. If a request is
     * submitted with a starting register address of 96 and a quantity of
     * registers of 5, then this request will fail with Exception Code 0x02
     * “Illegal Data Address” since it attempts to operate on registers 96, 97,
     * 98, 99 and 100, and there is no register with address 100.
     *
     * 03 ILLEGAL DATA VALUE (非法数据值)
     *
     * A value contained in the query data field is not an allowable value for
     * server (or slave). This indicates a fault in the structure of the
     * remainder of a complex request, such as that the implied length is
     * incorrect. It specifically does NOT mean that a data item submitted for
     * storage in a register has a value outside the expectation of the
     * application program, since the MODBUS protocol is unaware of the
     * significance of any particular value of any particular register.
     *
     * 04 SLAVE DEVICE FAILURE (从节点设备故障)
     *
     * An unrecoverable error occurred while the server (or slave) was
     * attempting to perform the requested action.
     *
     * 06 SLAVE DEVICE BUSY (从设备忙)
     *
     * Specialized use in conjunction with programming commands. The server (or
     * slave) is engaged in processing a long–duration program command. The
     * client (or master) should retransmit the message later when the server
     * (or slave) is free.
     *
     * 08 MEMORY PARITY ERROR (无权限)
     *
     * Specialized use in conjunction with function codes 20 and 21 and
     * reference type 6, to indicate that the extended file area failed to pass
     * a consistency check. The server (or slave) attempted to read record file,
     * but detected a parity error in the memory. The client (or master) can
     * retry the request, but service may be required on the server (or slave)
     * device.
     */

    private static final HashMap<Short, String> ERRORS = new HashMap<>();

    static {
        ERRORS.put((short) (0x01), "ILLEGAL FUNCTION");
        ERRORS.put((short) (0x02), "ILLEGAL DATA ADDRESS");
        ERRORS.put((short) (0x03), "ILLEGAL DATA VALUE");
        ERRORS.put((short) (0x04), "SLAVE DEVICE FAILURE");
        ERRORS.put((short) (0x06), "SLAVE DEVICE BUSY");
        ERRORS.put((short) (0x08), "MEMORY PARITY ERROR");
    }

    private short exceptionCode;
    private String exceptionMessage;

    public ModbusError(short functionCode) {
        super(functionCode);
    }

    public ModbusError(short functionCode, short exceptionCode) {
        super(functionCode);
        this.exceptionCode = exceptionCode;

    }

    private void setExceptionMessage(short exceptionCode) {
        this.exceptionMessage = ERRORS.get(exceptionCode) != null ? ERRORS.get(exceptionCode) : "UNDEFINED ERROR";
    }

    public short getExceptionCode() {
        return exceptionCode;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    @Override
    public int calculateLength() {
        return 1 + 1;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer(calculateLength());
        buf.writeByte(getFunctionCode());
        buf.writeByte(exceptionCode);

        return buf;
    }

    @Override
    public void decode(ByteBuf data) {
        exceptionCode = data.readUnsignedByte();

        setExceptionMessage(exceptionCode);
    }

    @Override
    public String toString() {
        return "ModbusError{" + "exceptionCode=" + exceptionCode + ", exceptionMessage=" + exceptionMessage + '}';
    }
}
