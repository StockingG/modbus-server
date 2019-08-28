package com.meller.modbusserver.handle;

import com.meller.modbusserver.entity.AbstractModbusFunction;
import com.meller.modbusserver.entity.ModbusFrame;
import com.meller.modbusserver.entity.ModbusHeader;
import com.meller.modbusserver.entity.func.ModbusError;
import com.meller.modbusserver.entity.func.response.ReadHoldingRegistersResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.meller.modbusserver.config.MobsConstants.MBAP_LENGTH;


/**
 * modbus 解码处理器
 *
 * @author chenleijun
 */
public class ModbusDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        /*Function Code*/
        if (buffer.capacity() < MBAP_LENGTH + 1) {
            return;
        }

        ModbusHeader mbapHeader = ModbusHeader.decode(buffer);

        short functionCode = buffer.readUnsignedByte();

        AbstractModbusFunction function = null;
        // 根据功能码 选择方法
        switch (functionCode) {
            case AbstractModbusFunction.READ_HOLDING_REGISTERS:
                function = new ReadHoldingRegistersResponse();
                break;
            default:
                break;
        }

        if (AbstractModbusFunction.isError(functionCode)) {
            function = new ModbusError(functionCode);
        } else if (function == null) {
            function = new ModbusError(functionCode, (short) 1);
        }

        function.decode(buffer.readBytes(buffer.readableBytes()));

        ModbusFrame frame = new ModbusFrame(mbapHeader, function);

        out.add(frame);
    }
}
