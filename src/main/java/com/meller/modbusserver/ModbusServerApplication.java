package com.meller.modbusserver;

import com.meller.modbusserver.server.ModbusServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ModbusServerApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(ModbusServerApplication.class, args);
        ModbusServer server = context.getBean(ModbusServer.class);
        server.start();
    }
}
