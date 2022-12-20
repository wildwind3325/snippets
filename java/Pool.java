// Druid
Properties props = new Properties();
props.setProperty("driverClassName", "org.apache.phoenix.jdbc.PhoenixDriver");
props.setProperty("url", "jdbc:phoenix:172.17.114.235,172.17.114.236,172.17.114.237:2181");
props.setProperty("maxActive", "10");
props.setProperty("minIdle", "1");
props.setProperty("maxWait", "10000");
props.setProperty("validationQuery", "SELECT 1");
ds = DruidDataSourceFactory.createDataSource(props);

Tomcat自带连接池配置方法
Context.xml文件加入：
<?xml version="1.0" encoding="UTF-8"?>
<Context path="/phoenix">
    <Resource
        name="jdbc/phoenix_connect"
        auth="Container"
        type="javax.sql.DataSource"
        driverClassName="org.apache.phoenix.jdbc.PhoenixDriver"
        url="jdbc:phoenix:172.17.114.235,172.17.114.236,172.17.114.237:2181"
        username=""
        password=""
        maxActive="10"
        maxIdle="3"
        maxWait="10000" />
</Context>
程序中引入：
Context ctx = new InitialContext();
DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/phoenix_connect");
获取连接：
Connection conn = ds.getConnection();