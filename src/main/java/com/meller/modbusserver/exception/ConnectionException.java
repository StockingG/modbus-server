package com.meller.modbusserver.exception;


/**
 * 连接异常
 *
 * @author chenleijun
 */
public class ConnectionException extends Exception {

    public ConnectionException(String message) {
        super(message);
    }
}
