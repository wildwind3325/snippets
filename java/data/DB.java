package org.clkg.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DB {

    DBConfig config;
    Connection conn;

    public DB(String name) {
        config = CM.find(name);
    }

    public void open() throws SQLException {
        conn = DriverManager.getConnection(config.url, config.user, config.password);
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

    public int execute(String sql, Object... args) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 1; i <= args.length; i++) {
            ps.setObject(i, args[i - 1]);
        }
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }

    public Object executeScalar(String sql, Object... args) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 1; i <= args.length; i++) {
            ps.setObject(i, args[i - 1]);
        }
        ResultSet rs = ps.executeQuery();
        Object obj = null;
        if (rs.next()) {
            obj = rs.getObject(1);
        }
        rs.close();
        ps.close();
        return obj;
    }

    public int insert(String table, Map<String, Object> map) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into `").append(table).append("` (");
        List args = new ArrayList();
        map.forEach((key, value) -> {
            if (!key.equalsIgnoreCase("id")) {
                if (args.size() > 0) {
                    sb.append(", ");
                }
                sb.append("`").append(key).append("`");
                args.add(value);
            }
        });
        sb.append(") values (");
        for (int i = 0; i < args.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(")");
        PreparedStatement ps = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
        for (int i = 1; i <= args.size(); i++) {
            ps.setObject(i, args.get(i - 1));
        }
        int result = ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            map.put("id", rs.getInt(1));
        }
        rs.close();
        ps.close();
        return result;
    }

    public int update(String table, Map<String, Object> map) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("update `").append(table).append("` set ");
        List args = new ArrayList();
        map.forEach((key, value) -> {
            if (!key.equalsIgnoreCase("id")) {
                if (args.size() > 0) {
                    sb.append(", ");
                }
                sb.append("`").append(key).append("` = ?");
                args.add(value);
            }
        });
        sb.append(" where `id` = ?");
        args.add(map.get("id"));
        PreparedStatement ps = conn.prepareStatement(sb.toString());
        for (int i = 1; i <= args.size(); i++) {
            ps.setObject(i, args.get(i - 1));
        }
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }

    public int delete(String table, int id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("delete from `" + table + "` where `id` = ?");
        ps.setObject(1, id);
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }

    public List<Map<String, Object>> find(String sql, Object... args) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 1; i <= args.length; i++) {
            ps.setObject(i, args[i - 1]);
        }
        ResultSet rs = ps.executeQuery();
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        List<String> fields = new ArrayList<>();
        for (int i = 0; i < meta.getColumnCount(); i++) {
            fields.add(meta.getColumnName(i + 1));
        }
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (String field : fields) {
                row.put(field, rs.getObject(field));
            }
            list.add(row);
        }
        rs.close();
        ps.close();
        return list;
    }

    public Map<String, Object> findByPage(int pageNumber, int pageSize, String sql, Object... args) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        PreparedStatement ps = conn.prepareStatement("select count(*) total" + sql.substring(sql.toLowerCase().indexOf(" from ")));
        for (int i = 1; i <= args.length; i++) {
            ps.setObject(i, args[i - 1]);
        }
        ResultSet rs = ps.executeQuery();
        rs.next();
        int total = rs.getInt("total");
        rs.close();
        ps.close();
        if (total <= pageSize * (pageNumber - 1)) {
            pageNumber = total / pageSize;
            if (total % pageSize != 0) {
                pageNumber++;
            }
        }
        int limit = pageSize * (pageNumber - 1);
        ps = conn.prepareStatement(sql + " limit " + limit + ", " + pageSize);
        for (int i = 1; i <= args.length; i++) {
            ps.setObject(i, args[i - 1]);
        }
        rs = ps.executeQuery();
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        List<String> fields = new ArrayList<>();
        for (int i = 0; i < meta.getColumnCount(); i++) {
            fields.add(meta.getColumnName(i + 1));
        }
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (String field : fields) {
                row.put(field, rs.getObject(field));
            }
            list.add(row);
        }
        rs.close();
        ps.close();
        result.put("total", total);
        result.put("rows", list);
        result.put("pageNumber", pageNumber);
        result.put("pageSize", pageSize);
        return result;
    }
}
