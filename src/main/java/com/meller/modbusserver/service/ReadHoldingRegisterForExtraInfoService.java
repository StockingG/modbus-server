package com.meller.modbusserver.service;

import com.meller.modbusserver.config.MobsConstants;
import com.meller.modbusserver.config.NettyConfig;
import com.meller.modbusserver.config.coon.Connection;
import com.meller.modbusserver.entity.client.ExtraInfo;
import com.meller.modbusserver.entity.client.Sn;
import com.meller.modbusserver.entity.event.ModbusMsg;
import com.meller.modbusserver.entity.mq.DCStruct;
import com.meller.modbusserver.entity.mq.WarningStruct;
import com.meller.modbusserver.utils.CommonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.meller.modbusserver.utils.ByteUtils.*;
import static com.meller.modbusserver.utils.CommonUtils.div;
/**
 * @author chenleijun
 * @Date 2018/6/12
 */
@Slf4j
@Service
public class ReadHoldingRegisterForExtraInfoService {

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
        Date date = new Date();
        conn.setLastAnnounceTime(date);
        String serialNumber = conn.getSn();

        if (serialNumber == null) {
            log.warn("this connection dont set SN");
            return;
        }

        int[] msg = modbusMsg.getData();
        ExtraInfo extraInfo = getExtraInfo(msg);
        log.info("decode int[] by ReadHoldingRegisterForExtraInfoService:" + extraInfo.toString());

        //发送逆变器直流侧数据
        DCStruct dc = new DCStruct();
        Sn sn = NettyConfig.cacheManager.getValue(serialNumber);
        if (sn == null || sn.getInverterCode() == null) {
            dc.setInverterCode(serialNumber);
        } else {
            dc.setInverterCode(sn.getInverterCode());
        }
        dc.setCode(CommonUtils.uuid());
        dc.setRecordTime(date);
        dataForwardService.sendToInverterDcSideQueue(dc);
        log.info("ReadHoldingRegisterForExtraInfoService send inverter_dc_side msg to mq, this msg is " + dc.toString());

        //如果没有pvPlantCode,不发送警告信息
        if (sn == null || sn.getPvPlantCode() == null) {
            log.warn("----warning!!! it need pv_plant_code to send warn to mq--------");
            return;
        }
        byte[] warn1 = extraInfo.getWarning1();
        byte[] warn2 = extraInfo.getWarning2();
        byte[] warn3 = extraInfo.getWarning3();
        sendWarningToMq(sn, warn1,1);
        sendWarningToMq(sn, warn2,2);
        sendWarningToMq(sn, warn3,3);

    }

    /**
     * 组合 warn 向rabbitmq发送警告
     *
     * @param sn
     * @param warns
     * @param n
     */
    private void sendWarningToMq(Sn sn, byte[] warns, int n) {
        WarningStruct warn = new WarningStruct();
        warn.setDeviceType("inverter");
        warn.setReportedAt(new Date());

        for (int i = 0; i < warns.length; i++) {
            if (warns[i] == 1) {
                String warningCategory = "HW-"+n+"-"+(i + 1);
                warn.setCode(CommonUtils.uuid());
                dataForwardService.sendToWarningQueue(warn);
                log.info("ReadHoldingRegisterForExtraInfoService send warn msg to mq, this msg is " + warn.toString());
            }
        }
    }

    /**
     * 转化 byte[] msg 信息
     *
     * @param msg
     * @return
     */
    private ExtraInfo getExtraInfo(int[] msg) {
        byte[] warning1 = getWarnArray(getByteArray(msg[0]));
        byte[] warning2 = getWarnArray(getByteArray(msg[1]));
        byte[] warning3 = getWarnArray(getByteArray(msg[2]));
        double voltage1 = div((short) msg[8], 10, 1);
        double voltage2 = div((short) msg[10], 10, 1);
        double voltage3 = div((short) msg[12], 10, 1);
        double voltage4 = div((short) msg[14], 10, 1);
        double current1 = div((short) msg[9], 100, 2);
        double current2 = div((short) msg[11], 100, 2);
        double current3 = div((short) msg[13], 100, 2);
        double current4 = div((short) msg[15], 100, 2);

        ExtraInfo extraInfo = new ExtraInfo();
        extraInfo.setWarning1(warning1);
        extraInfo.setWarning2(warning2);
        extraInfo.setWarning3(warning3);
        extraInfo.setVoltage1(voltage1);
        extraInfo.setVoltage2(voltage2);
        extraInfo.setVoltage3(voltage3);
        extraInfo.setVoltage4(voltage4);
        extraInfo.setCurrent1(current1);
        extraInfo.setCurrent2(current2);
        extraInfo.setCurrent3(current3);
        extraInfo.setCurrent4(current4);
        return extraInfo;
    }


}
