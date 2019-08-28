package com.meller.modbusserver.config.coon;

import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenleijun
 */
public class Connection {

    /**
     * 链接id,链接的唯一标示
     */
    private final String id;
    /**
     * 链接的ChannelHandlerContext
     */
    private ChannelHandlerContext ctx;

    /**
     * sn采集器序列号
     */
    private volatile String sn;

    /**
     * 创建时间
     */
    private volatile Date createTime;

    /**
     * 采集器最后在线时间
     */
    private volatile Date lastAnnounceTime;

    private boolean isSetSn = false;

    private Lock lock = new ReentrantLock();

    /**
     * 附带属性
     */
    private ConcurrentHashMap<String, Object> attr = new ConcurrentHashMap<String, Object>();

    public Object getAttr(String key) {
        return attr.get(key);
    }

    public Object putAttr(String key, Object value) {
        return attr.put(key, value);
    }

    public Object removeAttr(String key) {
        return attr.remove(key);
    }

    public Connection(String id, ChannelHandlerContext ctx,Date createTime, Date lastAnnounceTime) {
        this.id = id;
        this.ctx = ctx;
        this.createTime = createTime;
        this.lastAnnounceTime = lastAnnounceTime;
    }

    public String getId() {
        return id;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    /**
     * 线程安全,没有验验证通过前，返回null
     */
    public String getSn() {
        try {
            lock.lock();
            return sn;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置sn地址，只可以设置一次，线程安全
     */
    public boolean setSn(String sn) {
        try {
            lock.lock();
            if (isSetSn) {
                return false;
            }
            this.sn = sn;
            isSetSn = true;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        try {
            lock.lock();
            this.createTime = createTime;
        }finally {
            lock.unlock();
        }
    }

    public Date getLastAnnounceTime() {
        return lastAnnounceTime;
    }

    public void setLastAnnounceTime(Date lastAnnounceTime) {
        try {
            lock.lock();
            this.lastAnnounceTime = lastAnnounceTime;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Connection{" +
                "id='" + id + '\'' +
                ", ctx=" + ctx +
                ", sn='" + sn + '\'' +
                ", createTime=" + createTime +
                ", lastAnnounceTime=" + lastAnnounceTime +
                '}';
    }
}
