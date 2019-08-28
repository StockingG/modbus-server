package com.meller.modbusserver.web;

import com.meller.modbusserver.entity.pojo.Reply;

/**
 * @author chenleijun
 * @Date 2018/6/14
 */
public interface HandlerService {

    Reply getCurrentConn();

    Reply getCurrentListConn();
}
