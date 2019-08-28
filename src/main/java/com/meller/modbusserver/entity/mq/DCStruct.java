package com.meller.modbusserver.entity.mq;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 直流侧信息消息队列数据结构
 *
 * @author chenleijun
 * @Date 2018/6/11
 */
@Data
public class DCStruct implements Serializable {

    private static final long serialVersionUID = 1863566172987952257L;

    /**
     * 数据项UUID
     */
    private String code;

    /**
     * 逆变器编号，UUID
     */
    private String inverterCode;

    /**
     * 数据源引用
     */
    private String sourceRef = "Huawei 4G module";

    /**
     * 其他数据
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
