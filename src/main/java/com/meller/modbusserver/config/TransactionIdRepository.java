package com.meller.modbusserver.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储协议编号
 *
 * @author chenleijun
 * @Date 2018/6/8
 */
public class TransactionIdRepository {
    private final Map<String, String> responses = new ConcurrentHashMap<>(MobsConstants.TRANSACTION_IDENTIFIER_MAX);

    public TransactionIdRepository put(String key, String value) {
        responses.put(key, value);
        return this;
    }

    public String get(String key) {
        return responses.get(key);
    }

    public void remove(String key) {
        responses.remove(key);
    }
}
