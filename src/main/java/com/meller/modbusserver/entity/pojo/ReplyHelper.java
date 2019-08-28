package com.meller.modbusserver.entity.pojo;

/**
 * @author chenleijun
 */
public final class ReplyHelper {

    /**
     * 非法4XX
     */
    private static final Reply NO_AUTH_REPLY = new Reply();
    private static final Reply INVALID_TOKEN_REPLY = new Reply();

    private static final Reply INVALID_ACCOUNT_REPLY = new Reply();
    private static final Reply FORBID_REPLY = new Reply();
    private static final Reply OVERLOAD_REPLY = new Reply();

    private static final Reply INVALID_EMAIL_REPLY = new Reply();
    private static final Reply TOO_OFTEN_REPLY = new Reply();
    private static final Reply INVALID_PHONE_REPLY = new Reply();
    private static final Reply ACCOUNT_EXIST_REPLY = new Reply();

    private static final Reply INVALID_CODE_REPLY = new Reply();
    private static final Reply EXPIRED_TOKEN_REPLY = new Reply();

    /**
     * 请求成功
     *
     * @return Reply
     */
    public static Reply success() {
        Reply reply = new Reply();
        reply.setCode(200);
        reply.setSuccess(true);
        reply.setMessage("请求成功");

        return reply;
    }

    /**
     * 请求成功
     *
     * @param data 数据
     * @return Reply
     */
    public static Reply success(Object data) {
        Reply reply = new Reply();
        reply.setCode(200);
        reply.setSuccess(true);
        reply.setData(data);
        reply.setMessage("请求成功");

        return reply;
    }

    /**
     * 请求成功
     *
     * @param data   数据
     * @param option 选项
     * @return Reply
     */
    public static Reply success(Object data, Object option) {
        Reply reply = new Reply();
        reply.setCode(200);
        reply.setSuccess(true);
        reply.setData(data);
        reply.setOption(option);
        reply.setMessage("请求成功");

        return reply;
    }


    /**
     * 请求成功
     *
     * @param data   数据
     * @param option 选项
     * @param msg    提示信息
     * @return Reply
     */
    public static Reply success(Object data, Object option, String msg) {
        Reply reply = new Reply();
        reply.setCode(200);
        reply.setSuccess(true);
        reply.setData(data);
        reply.setOption(option);
        reply.setMessage(msg);

        return reply;
    }

    /**
     * 请求成功
     *
     * @param msg 消息
     * @return
     */
    public static Reply success(String msg) {
        Reply reply = new Reply();
        reply.setCode(200);
        reply.setSuccess(true);
        reply.setMessage(msg);

        return reply;
    }

    /**
     * 请求成功
     *
     * @param data 数据
     * @param msg  消息
     * @return
     */
    public static Reply success(Object data, String msg) {
        Reply reply = new Reply();
        reply.setCode(200);
        reply.setSuccess(true);
        reply.setData(data);
        reply.setMessage(msg);

        return reply;
    }


    /**
     * 服务端错误
     *
     * @return Reply
     */
    public static Reply fail() {
        Reply reply = new Reply();
        reply.setCode(400);
        reply.setSuccess(false);
        reply.setMessage("请求失败");

        return reply;
    }

    /**
     * @param msg 消息
     * @return
     */
    public static Reply fail(String msg) {
        Reply reply = new Reply();
        reply.setCode(400);
        reply.setSuccess(false);
        reply.setMessage(msg);

        return reply;
    }


    /**
     * 缺少参数
     *
     * @return Reply
     */
    public static Reply parametersCheckFalse() {
        Reply reply = new Reply();
        reply.setCode(4001);
        reply.setSuccess(false);
        reply.setMessage("参数校验不通过，请重新确认传参");

        return reply;
    }
}
