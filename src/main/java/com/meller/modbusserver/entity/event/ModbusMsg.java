package com.meller.modbusserver.entity.event;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author chenleijun
 * @Date 2018/6/12
 */
public class ModbusMsg {

    private ChannelHandlerContext ctx;
    private int[] data;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public ModbusMsg(ChannelHandlerContext ctx, int[] data) {
        this.ctx = ctx;
        this.data = data;
    }
}
