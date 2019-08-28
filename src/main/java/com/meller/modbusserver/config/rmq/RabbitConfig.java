package com.meller.modbusserver.config.rmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author chenleijun
 * @date 11/7/16
 */
@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.virtualhost}")
    private String virtualHost;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(this.host);
        factory.setPort(port);
        factory.setVirtualHost(this.virtualHost);
        factory.setUsername(this.username);
        factory.setPassword(this.password);

        return factory;
    }

    @Bean
    public Queue inverterDataQueue() {
        return new Queue(Environment.RMQ_Q_INVERTER_DATA);
    }

    @Bean
    public Queue inverterDcSideDataQueue() {
        return new Queue(Environment.RMQ_Q_INVERTER_DC_SIDE_DATA);
    }

    @Bean
    public Queue warningQueue() {
        return new Queue(Environment.RMQ_Q_WARNING);
    }

}
