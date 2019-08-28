package com.meller.modbusserver.web;

import com.meller.modbusserver.config.coon.Connection;
import com.meller.modbusserver.config.coon.ConnectionManager;
import com.meller.modbusserver.entity.pojo.Reply;
import com.meller.modbusserver.entity.pojo.ReplyHelper;
import com.meller.modbusserver.web.dto.ConnDto;
import com.meller.modbusserver.web.dto.HandlerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenleijun
 * @Date 2018/6/14
 */
@Slf4j
@Service
public class HandlerServiceImpl implements HandlerService {

    @Override
    public Reply getCurrentConn() {
        List<String> connLst = ConnectionManager.getInstance().listAllConn();
        HandlerDto handlerDto = new HandlerDto();
        handlerDto.setConnectionNumber(connLst.size());
        log.info("查看当前有" + connLst.size() + "个华为逆变器连接上来");
        return ReplyHelper.success(handlerDto);
    }

    @Override
    public Reply getCurrentListConn() {
        List<Connection> connectionList = ConnectionManager.getInstance().listConns();
        List<ConnDto> resultList = new ArrayList<>();
        for (Connection connection : connectionList) {
            ConnDto connDto = new ConnDto();
            connDto.setSn(connection.getSn());
            connDto.setCreateTime(connection.getCreateTime());
            connDto.setLastAnnounceTime(connection.getLastAnnounceTime());
            resultList.add(connDto);
        }
        return ReplyHelper.success(resultList);
    }
}
