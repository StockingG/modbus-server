package com.meller.modbusserver.entity.mq;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 逆变器消息队列数据结构
 *
 * @author chenleijun
 * @Date 2018/6/11
 */
@Data
public class InverterStruct implements Serializable {

    private static final long serialVersionUID = -5238943790489325182L;

    /**
     * 数据记录的编号UUID
     */
    private String code;

    /**
     * 逆变器编号，UUID
     */
    private String inverterCode;

    /**
     * 其他数据，JSON格式
     */
    private String otherData;

    /**
     * 数据获取时间
     */
    private Date recordTime;

    /**
     * 记录创建时间
     */
    private Date createTime;
}
