package com.meller.modbusserver.entity.client;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Hex;

import java.io.Serializable;

/**
 * @author chenleijun
 * @Date 2018/6/11
 */
@Data
public class ExtraInfo implements Serializable {

    private static final long serialVersionUID = 8715899814984953943L;

    /**
     * 告警1
     */
    private byte[] warning1;

    /**
     * 告警2
     */
    private byte[] warning2;

    /**
     * 告警3
     */
    private byte[] warning3;

    /**
     * pv1电压
     */
    private double voltage1;

    /**
     * pv2电压
     */
    private double voltage2;

    /**
     * pv3电压
     */
    private double voltage3;

    /**
     * pv4电压
     */
    private double voltage4;

    /**
     * pv1电流
     */
    private double current1;

    /**
     * pv2电流
     */
    private double current2;

    /**
     * pv3电流
     */
    private double current3;

    /**
     * pv4电流
     */
    private double current4;
}
