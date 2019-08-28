package com.meller.modbusserver.service;

import com.meller.modbusserver.config.coon.Connection;
import com.meller.modbusserver.config.MobsConstants;
import com.meller.modbusserver.config.NettyConfig;
import com.meller.modbusserver.entity.client.Sn;
import com.meller.modbusserver.entity.event.ModbusMsg;
import com.meller.modbusserver.utils.ByteUtils;
import com.meller.modbusserver.utils.IntUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author chenleijun
 * @Date 2018/6/11
 */
@Slf4j
@Service
public class ReadHoldingRegisterForSnService {

    /**
     * 业务代码进行处理
     *
     * @param modbusMsg 抽象业务模型
     */
    public void execute(ModbusMsg modbusMsg) {
        int[] msg = modbusMsg.getData();
        String hexString = IntUtils.intArrToHex(msg);
        byte[] byteArray = ByteUtils.hexStringToByteArray(hexString);
        String serialNumber = ByteUtils.byteArray2Str(byteArray);
        if (serialNumber == null) {
            log.error("the register never give SN");
            return;
        }
        log.info("the register return SN is: " + serialNumber);
        //将sn存入到全局变量，先为null值
        Sn sn = new Sn();
        NettyConfig.cacheManager.addOrUpdateCache(serialNumber, sn);

        //将sn存入connection中
        ChannelHandlerContext ctx = modbusMsg.getCtx();
        Attribute<Connection> attr = ctx.channel().attr(MobsConstants.CONN_KEY);
        Connection conn = attr.get();
        conn.setSn(serialNumber);
        Date date = new Date();
        conn.setLastAnnounceTime(date);
    }
}
