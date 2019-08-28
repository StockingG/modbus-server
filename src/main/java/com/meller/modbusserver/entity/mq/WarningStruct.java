package com.meller.modbusserver.entity.mq;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警信息消息队列数据结构
 *
 * @author chenleijun
 * @Date 2018/6/11
 */
@Data
public class WarningStruct implements Serializable {

    private static final long serialVersionUID = 5863688391035870482L;

    private String code;

    /**
     * 关联设备的类别
     * pv_plant,
     * inverter,
     * ammeter,
     * junction_box
     * <p>
     * 参照:  @see DeviceType
     */
    private String deviceType;

    /**
     * 是否已解决
     */
    private boolean hasFixed;

    /**
     * 　问题产生时间
     */
    private Date reportedAt;
}
