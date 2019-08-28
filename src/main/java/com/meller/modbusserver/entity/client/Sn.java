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
public class Sn implements Serializable {

    private static final long serialVersionUID = -1716264805805103450L;

    private String sn;

    private String inverterCode;

    private String pvPlantCode;
}
