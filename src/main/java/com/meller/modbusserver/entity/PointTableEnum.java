package com.meller.modbusserver.entity;

/**
 * @author chenleijun
 * @Date 2018/6/6
 */
public enum PointTableEnum {
    /**
     * 点表SN信息
     */
    SN(30015, 15),

    /**
     * 点表基本信息
     */
    BASICINFO(32080, 28),

    /**
     * 点表额外的信息
     */
    EXTRAINFO(32008, 16);

    /**
     * 寄存器地址
     */
    private int address;
    /**
     * 寄存器值
     */
    private int value;

    PointTableEnum(int address, int value) {
        this.address = address;
        this.value = value;
    }

    public int getAddress() {
        return address;
    }

    public int getValue() {
        return value;
    }

}
