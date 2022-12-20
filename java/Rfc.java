import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import java.util.Properties;

public class HVDestinationDataProvider implements DestinationDataProvider {

    @Override
    public Properties getDestinationProperties(String name) throws DataProviderException {
        return ConnManager.getProperties(name);
    }

    @Override
    public boolean supportsEvents() {
        return false;
    }

    @Override
    public void setDestinationDataEventListener(DestinationDataEventListener listener) {
    }
}

// 自定义连接信息管理
package com.skoito.service.sap;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.Environment;
import java.util.HashMap;
import java.util.Properties;

public class ConnManager {

    public static boolean registered = false;
    public static HashMap<String, Properties> props = new HashMap<String, Properties>();
    public static HashMap<String, JCoDestination> list = new HashMap<String, JCoDestination>();

    public static Properties getProperties(String name) {
        if (!props.containsKey(name)) {
            String[] sa = new String(WebUtil.httpDownload("http://172.17.114.42:8080/ecc/" + name + ".jcoDestination")).split("\r\n");
            Properties prop = new Properties();
            for (int i = 0; i < sa.length; i++) {
                if (sa[i].length() > 0 && !sa[i].startsWith("#") && sa[i].indexOf("=") > 0) {
                    int p = sa[i].indexOf("=");
                    prop.setProperty(sa[i].substring(0, p), sa[i].substring(p + 1));
                }
            }
            props.put(name, prop);
        }
        return props.get(name);
    }

    public static JCoDestination getJCoDestination(String name) throws JCoException {
        if (!registered) {
            try {
                Environment.registerDestinationDataProvider(new HVDestinationDataProvider());
            } catch (Exception ex) {
            }
            registered = true;
        }
        if (!list.containsKey(name)) {
            list.put(name, JCoDestinationManager.getDestination(name));
        }
        return list.get(name);
    }
}

// 简便版本
Properties prop = new Properties();
            prop.setProperty("jco.client.lang", "1");
            prop.setProperty("jco.client.client", "800");
            prop.setProperty("jco.client.passwd", "741852");
            prop.setProperty("jco.client.user", "PPBG");
            prop.setProperty("jco.client.sysnr", "50");
            prop.setProperty("jco.client.ashost", "172.17.116.95");
            prop.setProperty("jco.client.peak_limit", "100");
            prop.setProperty("jco.client.pool_capacity", "10");
            try {
                Constructor constructor = RfcDestination.class.getDeclaredConstructor(new Class[]{String.class, Properties.class, String.class});
                constructor.setAccessible(true);
                RfcDestination destination = (RfcDestination) constructor.newInstance("PRD", prop, getClass().getName());
                JCoFunction function = destination.getRepository().getFunction("ZWM_RFC_CP_STOCKVALUE");
                function.execute(destination);
                function = null;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }