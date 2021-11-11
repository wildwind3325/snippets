package org.clkg.data;

import java.util.HashMap;
import java.util.Map;

public class CM {

    public static Map<String, DBConfig> conns = new HashMap<>();

    public static boolean register(String name, String driver, String url, String user, String password) {
        try {
            Class.forName(driver);
            DBConfig config = new DBConfig(url, user, password);
            conns.put(name, config);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static DBConfig find(String name) {
        if (conns.containsKey(name)) {
            return conns.get(name);
        }
        return null;
    }
}
