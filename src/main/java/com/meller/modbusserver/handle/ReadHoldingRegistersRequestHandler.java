package com.meller.modbusserver.handle;

import com.meller.modbusserver.config.NettyConfig;
import com.meller.modbusserver.config.coon.Connection;
import com.meller.modbusserver.config.MobsConstants;
import com.meller.modbusserver.config.coon.ConnectionManager;
import com.meller.modbusserver.entity.AbstractModbusFunction;
import com.meller.modbusserver.entity.ModbusFrame;
import com.meller.modbusserver.entity.ModbusHeader;
import com.meller.modbusserver.entity.PointTableEnum;
import com.meller.modbusserver.entity.event.ModbusMsg;
import com.meller.modbusserver.entity.func.request.ReadHoldingRegistersRequest;
import com.meller.modbusserver.entity.func.response.ReadHoldingRegistersResponse;
import com.meller.modbusserver.exception.ConnectionException;
import com.meller.modbusserver.service.ReadHoldingRegisterForBasicInfoService;
import com.meller.modbusserver.service.ReadHoldingRegisterForExtraInfoService;
import com.meller.modbusserver.service.ReadHoldingRegisterForSnService;
import com.meller.modbusserver.utils.CommonUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.meller.modbusserver.config.MobsConstants.*;
import static com.meller.modbusserver.entity.AbstractModbusFunction.READ_HOLDING_REGISTERS;

/**
 * modbus请求处理器
 *
 * @author chenleijun
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ReadHoldingRegistersRequestHandler extends SimpleChannelInboundHandler<ModbusFrame> {

    @Autowired
    ReadHoldingRegisterForSnService snService;

    @Autowired
    ReadHoldingRegisterForBasicInfoService basicService;

    @Autowired
    ReadHoldingRegisterForExtraInfoService extraInfoService;

    /**
     * 当client连接到服务端,注册Connection
     *
     * @param ctx handler 的上下文对象
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        String channelKey = ctx.channel().remoteAddress().toString();
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Date date = new Date();
        Connection conn = connectionManager.getNewConnection(channelKey,ctx,date,date);
        connectionManager.addToConns(channelKey, conn);
        Attribute<Connection> connAttr = ctx.channel().attr(MobsConstants.CONN_KEY);
        connAttr.set(conn);
        log.info("register a channel and a connection========");
    }

    /**
     * 当client处于活动状态
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String channelKey = ctx.channel().remoteAddress().toString();
        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(PointTableEnum.SN.getAddress(), PointTableEnum.SN.getValue());
        int transactionId = CommonUtils.calculateTransactionIdentifier();
        /* 区分不同channel的 client远程地址+head的传输标识符**/
        String transactionKey = channelKey + transactionId;
        NettyConfig.transactionIdRepository.addOrUpdateCache(transactionKey, SERIALNUMBER);
        int pduLength = request.calculateLength();
        ModbusHeader header = new ModbusHeader(transactionId, MobsConstants.DEFAULT_PROTOCOL_IDENTIFIER,
                pduLength, MobsConstants.DEFAULT_UNIT_IDENTIFIER);
        ModbusFrame firstMessage = new ModbusFrame(header, request);

        // 需要在当前线程马上发送
        ctx.channel().writeAndFlush(firstMessage);
    }

    /**
     * 当client离开server，channel不再被调用
     *
     * @param ctx
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        Attribute<Connection> attr = ctx.channel().attr(MobsConstants.CONN_KEY);
        String channelKey = ctx.channel().remoteAddress().toString();
        log.info("remove a connetion:{}, from connectionmanager", channelKey);
        ConnectionManager.getInstance().removeConn(channelKey);
        log.info("remove a connection======");
    }

    /**
     * channel出现异常，执行的方法
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("ReadHoldingRegistersRequestHandler 异常", cause);
        log.info("close channel");
        ctx.pipeline().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ModbusFrame response) throws ConnectionException {

        AbstractModbusFunction function = response.getFunction();
        short functionCode = function.getFunctionCode();
        if (functionCode != READ_HOLDING_REGISTERS) {
            throw new UnsupportedOperationException("Function not supported!");
        }
        ReadHoldingRegistersResponse readHoldingRegistersResponse = (ReadHoldingRegistersResponse) function;

        String channelKey = ctx.channel().remoteAddress().toString();
        int transactionId = response.getHeader().getTransactionIdentifier();
        String transactionKey = channelKey + transactionId;
        String info = NettyConfig.transactionIdRepository.getValue(transactionKey);
        if (StringUtils.isBlank(info)) {
            throw new ConnectionException("TransactionId not exist!");
        }

        int[] data = readHoldingRegistersResponse.getRegisters();

        ModbusMsg modbusMsg = new ModbusMsg(ctx, data);

        if (SERIALNUMBER.equals(info)) {
            snService.execute(modbusMsg);
        } else if (BASICINFO.equals(info)) {
            basicService.execute(modbusMsg);
        } else if (EXTRAINFO.equals(info)) {
            extraInfoService.execute(modbusMsg);
        }

        NettyConfig.transactionIdRepository.evictCache(transactionKey);
        log.info("remove seedKey from transactionIdRepository,the key is "+transactionKey);
    }
}

