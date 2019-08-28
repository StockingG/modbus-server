package com.meller.modbusserver.config.coon;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 链接管理类，spring中单例
 *
 * @author chenleijun
 */
public class ConnectionManager {

    private static final ConnectionManager INSTANCE = new ConnectionManager();

    public static ConnectionManager getInstance() {
        return INSTANCE;
    }

    private ConnectionManager() {
    }

    private ConcurrentHashMap<String, Connection> conns = new ConcurrentHashMap<String, Connection>();

    public Connection getNewConnection(String key, ChannelHandlerContext ctx, Date createTime, Date lastAnnounceTime) {
        Connection conn = new Connection(key, ctx,createTime,lastAnnounceTime);
        return conn;
    }

    public Enumeration<String> keys() {
        return conns.keys();
    }

    public List<String> listAllConn() {
        Enumeration<String> keys = conns.keys();
        List<String> connLst = new ArrayList<String>();
        while (keys.hasMoreElements()) {
            Connection c = conns.get(keys.nextElement());
            if (c != null) {
                connLst.add(c.getSn());
            }
        }
        return connLst;
    }

    public List<Connection> listConns() {
        Enumeration<String> keys = conns.keys();
        List<Connection> connLst = new ArrayList<Connection>();
        while (keys.hasMoreElements()) {
            Connection c = conns.get(keys.nextElement());
            if (c != null) {
                connLst.add(c);
            }
        }
        return connLst;
    }

    public Connection addToConns(String key, Connection conn) {
        Connection oldConn = conns.put(key, conn);
        if (oldConn == null) {
            // 新的key
            return null;
        }
        ChannelHandlerContext ctx = oldConn.getCtx();
        if (ctx == null) {
            // oldConn无效
            return oldConn;
        }
        if (oldConn.equals(conn)) {
            // 同key同value
            return null;
        }
        // 被替换的conn必须关闭
        ctx.pipeline().close();
        return oldConn;
    }

    public Connection getConn(String key) {
        return conns.get(key);
    }

    public Connection removeConn(String key) {
        return conns.remove(key);
    }

    public Connection removeConn(Connection conn) {
        Enumeration<String> keys = conns.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Connection v = conns.get(key);
            if (conn.equals(v)) {
                return conns.remove(key);
            }
        }
        return null;
    }

    public void closeAllConn() {
        Enumeration<String> keys = conns.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Connection v = conns.get(key);
            v.getCtx().close();
        }
    }

}
