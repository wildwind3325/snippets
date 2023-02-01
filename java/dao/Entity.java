package com.hv.data;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Entity {

    public static void parseEntity(Object obj, Map map) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Object newObj = Json.deserialize(Json.serialize(map), obj.getClass());
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Field field = newObj.getClass().getDeclaredField(entry.getKey().toString());
            field.set(obj, field.get(newObj));
        }
    }

    public static void insert(DB db, Object obj) throws IllegalArgumentException, IllegalAccessException, SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into `").append(obj.getClass().getSimpleName()).append("` (");
        Field[] fields = obj.getClass().getDeclaredFields();
        Field idField = null;
        Object[] args = new Object[fields.length - 1];
        int count = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equalsIgnoreCase("id")) {
                idField = field;
            } else {
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append("`").append(field.getName()).append("`");
                args[count] = field.get(obj);
                count++;
            }
        }
        sb.append(") values (");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(")");
        int id = db.executeInsert(sb.toString(), args);
        idField.set(obj, id);
    }

    public static void update(DB db, Object obj) throws IllegalArgumentException, IllegalAccessException, SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("update `").append(obj.getClass().getSimpleName()).append("` set ");
        Field[] fields = obj.getClass().getDeclaredFields();
        Field idField = null;
        Object[] args = new Object[fields.length];
        int count = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equalsIgnoreCase("id")) {
                idField = field;
            } else {
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append("`").append(field.getName()).append("` = ?");
                args[count] = field.get(obj);
                count++;
            }
        }
        sb.append(" where `id` = ?");
        args[count] = idField.get(obj);
        db.executeSql(sb.toString(), args);
    }

    public static int delete(DB db, Object obj) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, SQLException {
        Field field = obj.getClass().getDeclaredField("id");
        field.setAccessible(true);
        String sql = "delete from `" + obj.getClass().getSimpleName() + "` where `id` = ?";
        return db.executeSql(sql, field.get(obj));
    }

    public static <T> T findObjectByConditions(DB db, Filter filter, Sort sort, Class<T> cls) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException {
        String sql = "select * from `" + cls.getSimpleName() + "`";
        Object[] args;
        if (filter != null && !filter.getWhere().isEmpty()) {
            sql += " where " + filter.getWhere();
            args = filter.getArgs().toArray();
        } else {
            args = new Object[0];
        }
        if (sort != null) {
            sql += sort.getSortString();
        }
        ArrayList<T> list = db.executeQuery(cls, sql, args);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static <T> ArrayList<T> findObjectsByConditions(DB db, Filter filter, Sort sort, Class<T> cls) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException {
        String sql = "select * from `" + cls.getSimpleName() + "`";
        Object[] args;
        if (filter != null && !filter.getWhere().isEmpty()) {
            sql += " where " + filter.getWhere();
            args = filter.getArgs().toArray();
        } else {
            args = new Object[0];
        }
        if (sort != null) {
            sql += sort.getSortString();
        }
        return db.executeQuery(cls, sql, args);
    }

    public static <T> HashMap<String, Object> findObjectsByConditions(DB db, Filter filter, Pagination pagination, Sort sort, Class<T> cls) throws IllegalArgumentException, IllegalAccessException, InstantiationException, SQLException {
        HashMap<String, Object> ret = new HashMap<>();
        String sql = "select count(*) as total from `" + cls.getSimpleName() + "`";
        Object[] args;
        if (filter != null && !filter.getWhere().isEmpty()) {
            sql += " where " + filter.getWhere();
            args = filter.getArgs().toArray();
        } else {
            args = new Object[0];
        }
        Object obj = db.executeQuery(sql, args);
        int total = Integer.parseInt(obj.toString());
        ret.put("total", total);
        int size = 0;
        int number = 0;
        if (pagination != null) {
            size = pagination.getPageSize();
            number = pagination.getPageNumber();
            if (total <= size * (number - 1)) {
                number = total / size;
                if (total % size != 0) {
                    number++;
                }
            }
        }
        sql = "select * from `" + cls.getSimpleName() + "`";
        if (filter != null && !filter.getWhere().isEmpty()) {
            sql += " where " + filter.getWhere();
        }
        if (sort != null) {
            sql += sort.getSortString();
        } else {
            sql += " order by `id` asc";
        }
        if (pagination != null) {
            int start = size * (number - 1);
            sql += " limit " + start + ", " + size;
        }
        ArrayList<T> list = db.executeQuery(cls, sql, args);
        ret.put("rows", list);
        return ret;
    }
}
