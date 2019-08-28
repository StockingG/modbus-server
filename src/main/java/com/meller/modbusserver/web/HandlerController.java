package com.meller.modbusserver.web;

import com.meller.modbusserver.entity.pojo.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author chenleijun
 * @Date 2018/6/14
 */
@RestController
@RequestMapping("/api/v1.0/netty/handler")
public class HandlerController {

    @Autowired
    HandlerService handlerService;

    @GetMapping("")
    public Reply getNettyConn() {
        return handlerService.getCurrentConn();
    }

    @GetMapping("/list")
    public Reply getNettyConnList() {
        return handlerService.getCurrentListConn();
    }
}
