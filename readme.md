#  modebus-server
### 简要说明
本项目主要用于远程采集华为逆变器使用modbus tcp协议进行通讯的设备数据。
主要使用对象是华为逆变器。
主要采用对接文档是 SUN2000L V100R001 MODBUS 接口定义描述
采用netty 4.1.22.Final 版本作为高并发的底层开发依赖，通过TCP协议建立服务器与多个客户端的长链接通信，经测试可达1w链接。
同时提供web页面管理接口，可以查看客户端连接情况。


### 编译打包
项目有三个环境：dev,test,prod.
进入项目，确认权限，根据所需环境采用maven打包.
mvn clean package -P dev
mvn clean package -P test
mvn clean package -P prod