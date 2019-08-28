package com.meller.modbusserver.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chenleijun
 * @Date 2018/6/14
 */
@Getter
@Setter
public class HandlerDto {

    /**
     * 华为 client 连接数
     */
    private Integer connectionNumber;
}
