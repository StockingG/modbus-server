package com.meller.modbusserver.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author chenleijun
 * @Date 2018/7/6
 */
@Getter
@Setter
public class ConnDto {
    /**
     * 华为 client sn 序号
     */
    private String sn;

    /**
     * 华为 client 联网时间
     */
    private Date createTime;

    /**
     * 华为 client 最后在线时间
     */
    private Date lastAnnounceTime;
}
