package com.meller.modbusserver.exception;

import com.meller.modbusserver.entity.func.ModbusError;

/**
 * 错误响应码异常
 *
 * @author chenleijun
 */
public class ErrorResponseException extends Exception {

    int exceptionCode;

    public ErrorResponseException(ModbusError function) {
        super(function.toString());
    }
}
