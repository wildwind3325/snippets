package com.hv.data;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB {

    static final String url = "jdbc:mysql://172.17.114.236:3306/sconfig?useUnicode=true&characterEncoding=utf8&useSSL=false";
    static final String user = "admin";
    static final String password = "admin";

    Connection conn;

    public void open() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, password);
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (Exception ex) {
            System.out.println("关闭数据库连接发生异常：" + ex.getMessage());
        }
    }

    public int executeInsert(String sql, Object... args) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        for (int i = 1; i <= args.length; i++) {
            ps.setObject(i, args[i - 1]);
        }
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int ret = rs.getInt(1);
        ps.close();
        return ret;
    }

    public int executeSql(String sql, Object... args) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 1; i <= args.length; i++) {
            ps.setObject(i, args[i - 1]);
        }
        int ret = ps.executeUpdate();
        ps.close();
        return ret;
    }

    public Object executeQuery(String sql, Object... args) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 1; i <= args.length; i++) {
            ps.setObject(i, args[i - 1]);
        }
        ResultSet rs = ps.executeQuery();
        Object ret = null;
        if (rs.next()) {
            ret = rs.getObject(1);
        }
        ps.close();
        return ret;
    }

    public <T> ArrayList<T> executeQuery(Class<T> cls, String sql, Object... args) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 1; i <= args.length; i++) {
            ps.setObject(i, args[i - 1]);
        }
        ResultSet rs = ps.executeQuery();
        ArrayList<T> list = new ArrayList<>();
        Field[] fields = cls.getDeclaredFields();
        while (rs.next()) {
            T t = cls.newInstance();
            for (Field field : fields) {
                field.set(t, rs.getObject(field.getName()));
            }
            list.add(t);
        }
        return list;
    }
}
