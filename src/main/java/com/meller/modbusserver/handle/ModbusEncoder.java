package com.meller.modbusserver.handle;

import com.meller.modbusserver.entity.ModbusFrame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * modbus 编码处理器
 *
 * @author chenleijun
 */
public class ModbusEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ModbusFrame) {
            ModbusFrame frame = (ModbusFrame) msg;
            ctx.writeAndFlush(frame.encode());
        } else {
            ctx.write(msg);
        }
    }
}
