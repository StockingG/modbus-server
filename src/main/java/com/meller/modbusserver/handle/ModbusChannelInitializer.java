package com.meller.modbusserver.handle;

import com.meller.modbusserver.config.MobsConstants;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Modbus 服务器通道初始化器
 *
 * @author chenleijun
 * @Date 2018/6/1
 */
@Component
@Qualifier("modbusChannelInitializer")
public class ModbusChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    @Qualifier("readHoldingRegistersRequestHandler")
    private ChannelInboundHandlerAdapter readHoldingRegistersRequestHandler;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        /*
         * Modbus TCP Frame Description
         * - 最大。 256 字节（ADU = 7 字节 MBAP + 249 字节 PDU）
         * - 长度字段包括单元标识符 + PDU
         * <----------------------------------------------- ADU-------------------------------------------------------->
         * <---------------------------- MBAP ------------------- ----------------------> <------------- PDU ------------ - >
         * + ------------------------ + --------------------- + - --------- +-----------------++---------------+ --------------- +
         * | 传输标识符 | 协议标识符 | 长度 | 单位标识符 || 功能代码 | 数据 |
         * | （2 字节）| （2 字节）| （2 字节）| （1 字节）|| （1 字节）| （1 - 248 字节 |
         * + ------------------------ + --------------------- + - --------- +-----------------++---------------+ --------------- +
         */
        pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(MobsConstants.ADU_MAX_LENGTH, 4, 2));

        //Modbus encoder, decoder
        pipeline.addLast("encoder", new ModbusEncoder());
        pipeline.addLast("decoder", new ModbusDecoder());

        //server
        pipeline.addLast("requestHandler", readHoldingRegistersRequestHandler);
    }
}
