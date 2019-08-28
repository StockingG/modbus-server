package com.meller.modbusserver.scheduleJob;

import com.alibaba.fastjson.JSONObject;
import com.meller.modbusserver.config.NettyConfig;
import com.meller.modbusserver.entity.client.Sn;
import com.meller.modbusserver.entity.pojo.PvMapping;
import com.meller.modbusserver.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author chenleijun
 * @Date 2018/6/12
 */
@Slf4j
@Component
public class PvDeviceInfoFlushJob {

    private static final String RESULT = "result";
    private static final String CODE = "code";
    private static final String CODEVALUE = "10000";

    @Value("${varifySNUrl}")
    private String varifyTicketUrl;

    @Value("${snToken}")
    private String snToken;


    @Scheduled(cron = "0 0/5 * * * ?")
    public void executePvDeviceInfoFlushTask() {
        log.info("------PvDeviceInfoFlushTask Schedule start work------");
        List<Sn> list = checkOutPvDevice();

        //检测PHP调用接口是否正确返回值
        if(list == null){
           log.warn("PHP接口没有正确返回数据");
           log.info("------PvDeviceInfoFlushTask Schedule finish work------");
           return;
        }

        if(list.size() == 0){
            log.info("PHP接口没有返回数据");
            log.info("------PvDeviceInfoFlushTask Schedule finish work------");
            return;
        }

        for (Sn sn : list) {
            NettyConfig.cacheManager.addOrUpdateCache(sn.getSn(), sn);
        }
        log.info("------PvDeviceInfoFlushTask Schedule finish work------");
    }

    /**
     * 从开放PHP的API获取，SN和对应的pv_plant_coode inverter_code
     *
     * @return
     */
    private List<Sn> checkOutPvDevice() {
        //组合参数，请求openApi获取数据
        Map<String, String> headerParam = new HashMap<>();
        headerParam.put("token", snToken);
        headerParam.put("Content-Type", "application/json");
        String response = HttpClientUtil.httpClientGet(varifyTicketUrl, headerParam, "utf-8");

        //检验返回是否成功
        String code = getValue(response,CODE);
        if(!CODEVALUE.equals(code)){
            return null;
        }

        //取出result对应数据t,转化为对象
        String result = getValue(response, RESULT);
        List<PvMapping> resultList =JSONObject.parseArray(result,PvMapping.class);

        List<Sn> list = new ArrayList<>();
        if(resultList.size()==0){
            return list;
        }

        //组合返回Sn信息
        for (PvMapping pvMapping : resultList) {
            Sn sn = new Sn();
            sn.setSn(pvMapping.getSn());
            sn.setInverterCode(pvMapping.getDeviceCode());
            sn.setPvPlantCode(pvMapping.getPlantCode());
            list.add(sn);
        }
        return list;
    }


     private  String getValue(String json, String name){
         JSONObject jsonObject = JSONObject.parseObject(json);
         return jsonObject.getString(name);
     }
}
