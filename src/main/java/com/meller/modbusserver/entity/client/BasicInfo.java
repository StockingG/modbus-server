package com.meller.modbusserver.entity.client;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author chenleijun
 * @Date 2018/6/11
 */
@Data
public class BasicInfo implements Serializable {

    private static final long serialVersionUID = -1579604875021783442L;
    /**
     * 有功功率
     */
    private double activePower;

    /**
     * 无功功率
     */
    private double reactivePower;

    /**
     * 功率因数
     */
    private double factor;

    /**
     * 效率
     */
    private double efficiency;

    /**
     * 内部温度
     */
    private double temperature;

    /**
     * 设备状态
     */
    private double status;

    /**
     * 累计发电量
     */
    private double totalEnergy;
}
