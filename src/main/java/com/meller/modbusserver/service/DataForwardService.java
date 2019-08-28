package com.meller.modbusserver.service;

import com.meller.modbusserver.config.rmq.Environment;
import com.meller.modbusserver.entity.mq.DCStruct;
import com.meller.modbusserver.entity.mq.InverterStruct;
import com.meller.modbusserver.entity.mq.WarningStruct;
import com.meller.modbusserver.utils.Json;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenleijun
 * @Date 2018/6/13
 */
@Service
public class DataForwardService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendToInverterQueue(InverterStruct inverterData) {
        sendToQueue(Environment.RMQ_Q_INVERTER_DATA, inverterData);
    }

    public void sendToInverterDcSideQueue(DCStruct inverterDcSideData) {
        sendToQueue(Environment.RMQ_Q_INVERTER_DC_SIDE_DATA, inverterDcSideData);
    }

    public void sendToWarningQueue(WarningStruct warning) {
        sendToQueue(Environment.RMQ_Q_WARNING, warning);
    }

    private <T> void sendToQueue(String env, T data) {
        String message = Json.toJson(data);
        amqpTemplate.convertAndSend(env, message);
    }

}
