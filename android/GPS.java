package com.clkg.androidtest;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbGPS = findViewById(R.id.tbGPS);
        tbBT = findViewById(R.id.tbBT);
        tbBLE = findViewById(R.id.tbBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            if (checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH}, 1);
            }
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 1);
            }
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
            }
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

        BluetoothManager bm = (BluetoothManager) getBaseContext().getSystemService(Context.BLUETOOTH_SERVICE);
        bleAdapter = bm.getAdapter();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.btnGPS)) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null) {
                        tbBT.append("GPS定位不可用\r\n");
                        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location == null) {
                            tbBT.append("网络定位不可用\r\n");
                            location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        }
                    }
                    if (location != null) {
                        tbGPS.setText("维度：" + location.getLatitude() + "，经度：" + location.getLongitude());
                    } else {
                        tbBT.append("关联定位不可用\r\n");
                    }
                } catch (Exception ex) {
                    tbGPS.setText(ex.getMessage());
                    StackTraceElement[] elems = ex.getStackTrace();
                    for (int i = 0; i < elems.length; i++) {
                        tbBT.append(elems[i].toString() + "\r\n");
                    }
                }
            } else {
                Toast.makeText(this, "GPS定位功能未启用", Toast.LENGTH_LONG).show();
            }
        } else if (view == findViewById(R.id.btnBT)) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
            tbBT.setText("");
            bluetoothAdapter.startDiscovery();
            Toast.makeText(this, "开始搜索", Toast.LENGTH_LONG).show();
        } else if (view == findViewById(R.id.btnBLE)) {
            if (!bleAdapter.isEnabled()) {
                bleAdapter.enable();
            }
            if (!scaning) {
                tbBLE.setText("");
                list = new JSONObject();
                bleAdapter.startLeScan(scanCallback);
                Toast.makeText(this, "开始搜索", Toast.LENGTH_LONG).show();
            } else {
                bleAdapter.stopLeScan(scanCallback);
                Toast.makeText(this, "搜索完成", Toast.LENGTH_LONG).show();
            }
            scaning = !scaning;
        } else if (view == findViewById(R.id.btnPattern)) {
            tbBLE.setText(bluetoothAdapter.getAddress() + "\r\n");
            tbBLE.append(getBtAddressByReflection() + "\r\n");
            //tbBLE.append(initEquipData() + "\r\n");
        }
    }

    public String getBtAddressByReflection() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Field field = null;
        try {
            field = BluetoothAdapter.class.getDeclaredField("mService");
            field.setAccessible(true);
            Object bluetoothManagerService = field.get(bluetoothAdapter);
            if (bluetoothManagerService == null) {
                return null;
            }
            Method method = bluetoothManagerService.getClass().getMethod("getAddress");
            if (method != null) {
                Object obj = method.invoke(bluetoothManagerService);
                if (obj != null) {
                    return obj.toString();
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String initEquipData() {
        String result = post("http://172.17.114.76:8081/home/index/getInitEquipData", "address=" + bleAdapter.getAddress()).toUpperCase();
        try {
            JSONObject data = new JSONObject(result);
            if (data.has("status") && "200".equals(data.getString("status"))) {
                return data.getString("equipData").toUpperCase();
            } else {
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String post(String url, String body) {
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            writer = new PrintWriter(conn.getOutputStream());
            writer.print(body);
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String ret = "";
            String line;
            while ((line = reader.readLine()) != null) {
                ret += line;
            }
            return ret;
        } catch (Exception ex) {
            Log.e("MESAPP", ex.toString());
            return "{\"status\":\"201\",\"message\":\"" + ex.toString() + "\"}";
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception ex) {
                Log.e("MESAPP", ex.getMessage());
            }
        }
    }

    TextView tbGPS;
    EditText tbBT;
    EditText tbBLE;
    BluetoothAdapter bluetoothAdapter;
    BluetoothAdapter bleAdapter;
    boolean scaning = false;
    JSONObject list;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                tbBT.append(device.getName() + " | " + device.getAddress() + " | " + rssi + "\r\n");
                Log.i("BlueTooth", device.getName() + " | " + device.getAddress() + " | " + rssi);
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Toast.makeText(MainActivity.this, "搜索完成", Toast.LENGTH_LONG).show();
            }
        }
    };

    private BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!list.has(device.getAddress())) {
                        try {
                            list.put(device.getAddress(), rssi);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tbBLE.append(device.getName() + " | " + device.getAddress() + " | " + rssi + "\r\n");
                    }
                }
            });
        }
    };
}