package com.meller.modbusserver.entity.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenleijun
 */
@Data
public class PageConfigDTO implements Serializable {
    /**
     * 第几页
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 偏移量
     */
    private Integer offset;
}
