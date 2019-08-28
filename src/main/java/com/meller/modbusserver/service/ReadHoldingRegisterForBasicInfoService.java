package com.meller.modbusserver.service;

import com.meller.modbusserver.config.MobsConstants;
import com.meller.modbusserver.config.NettyConfig;
import com.meller.modbusserver.config.coon.Connection;
import com.meller.modbusserver.entity.client.BasicInfo;
import com.meller.modbusserver.entity.client.Sn;
import com.meller.modbusserver.entity.event.ModbusMsg;
import com.meller.modbusserver.entity.mq.InverterStruct;
import com.meller.modbusserver.utils.CommonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.meller.modbusserver.utils.CommonUtils.div;
import static com.meller.modbusserver.utils.IntUtils.getSign32;
import static com.meller.modbusserver.utils.IntUtils.getUnSign32;

/**
 * @author chenleijun
 * @Date 2018/6/12
 */
@Slf4j
@Service
public class ReadHoldingRegisterForBasicInfoService {

    @Autowired
    private DataForwardService dataForwardService;

    /**
     * 业务代码进行处理
     *
     * @param modbusMsg 抽象业务模型
     */
    public void execute(ModbusMsg modbusMsg) {
        ChannelHandlerContext ctx = modbusMsg.getCtx();
        Attribute<Connection> attr = ctx.channel().attr(MobsConstants.CONN_KEY);
        Connection conn = attr.get();
        String serialNumber = conn.getSn();
        Date date = new Date();
        conn.setLastAnnounceTime(date);

        if (serialNumber == null) {
            log.warn("this connection don't set SN");
            return;
        }

        int[] msg = modbusMsg.getData();
        BasicInfo basicInfo = getBasicInfo(msg);
        log.info("decode int[] by ReadHoldingRegisterForBaseInfoService:" + basicInfo.toString());

        InverterStruct inverter = new InverterStruct();
        Sn sn = NettyConfig.cacheManager.getValue(serialNumber);
        if (sn.getInverterCode() == null) {
            inverter.setInverterCode(serialNumber);
        } else {
            inverter.setInverterCode(sn.getInverterCode());
        }

        //消息发送到mq
        dataForwardService.sendToInverterQueue(inverter);
        log.info("ReadHoldingRegisterForBasicInfoService send inverter msg to mq, this msg is " + inverter.toString());
    }

    private BasicInfo getBasicInfo(int[] msg) {
        double activePower = getSign32(msg[0],msg[1]);
        double reactivePower = getSign32(msg[2],msg[3]);
        double factor = div((short) msg[4],1000,2);
        double efficiency = div(msg[6], 100, 2);
        double temperature = div(msg[7], 10, 1);
        double status = msg[9];
        double totalEnergy = div(getUnSign32(msg[26],msg[27]), 100, 2);

        BasicInfo basicInfo = new BasicInfo();
        basicInfo.setActivePower(activePower);
        basicInfo.setReactivePower(reactivePower);
        basicInfo.setFactor(factor);
        basicInfo.setTemperature(temperature);
        basicInfo.setEfficiency(efficiency);
        basicInfo.setStatus(status);
        basicInfo.setTotalEnergy(totalEnergy);

        return basicInfo;
    }
}
