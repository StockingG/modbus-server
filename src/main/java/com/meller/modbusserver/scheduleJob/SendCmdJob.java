package com.meller.modbusserver.scheduleJob;

import com.meller.modbusserver.config.NettyConfig;
import com.meller.modbusserver.config.coon.Connection;
import com.meller.modbusserver.config.MobsConstants;
import com.meller.modbusserver.config.coon.ConnectionManager;
import com.meller.modbusserver.entity.ModbusFrame;
import com.meller.modbusserver.entity.ModbusHeader;
import com.meller.modbusserver.entity.PointTableEnum;
import com.meller.modbusserver.entity.func.request.ReadHoldingRegistersRequest;
import com.meller.modbusserver.utils.CommonUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.meller.modbusserver.config.MobsConstants.*;


/**
 * 定时发送命令给client
 *
 * @author chenleijun
 * @Date 2018/6/12
 */
@Slf4j
@Component
public class SendCmdJob {

    @Scheduled(cron = "0 0/5 * * * ?")
    public void executeSendCmdTask() {
        log.info("------SendCmdTask Schedule start work------");
        ReadHoldingRegistersRequest baseInfoRequest = new ReadHoldingRegistersRequest(PointTableEnum.BASICINFO.getAddress(),
                PointTableEnum.BASICINFO.getValue());

        ReadHoldingRegistersRequest extraInfoRequest = new ReadHoldingRegistersRequest(PointTableEnum.EXTRAINFO.getAddress(),
                PointTableEnum.EXTRAINFO.getValue());

        ConnectionManager connectionManager = ConnectionManager.getInstance();
        List<Connection> list = connectionManager.listConns();
        for (Connection connection : list) {
            ChannelHandlerContext ctx = connection.getCtx();
            ModbusFrame baseMsg = getMsg(baseInfoRequest, connection.getId(), BASICINFO);
            ctx.write(baseMsg);
            ModbusFrame extraMsg = getMsg(extraInfoRequest, connection.getId(), EXTRAINFO);
            ctx.write(extraMsg);
        }
        log.info("------SendCmdTask Schedule finish work------");
    }

    private ModbusFrame getMsg(ReadHoldingRegistersRequest request, String channelKey, String mark) {
        int transactionId = CommonUtils.calculateTransactionIdentifier();
        /* 区分不同channel的 client远程地址+head的传输标识符**/
        String transactionKey = channelKey + transactionId;
        NettyConfig.transactionIdRepository.addOrUpdateCache(transactionKey, mark);
        int pduLength = request.calculateLength();
        ModbusHeader header = new ModbusHeader(transactionId, MobsConstants.DEFAULT_PROTOCOL_IDENTIFIER,
                pduLength, MobsConstants.DEFAULT_UNIT_IDENTIFIER);
        ModbusFrame msg = new ModbusFrame(header, request);
        return msg;
    }
}
