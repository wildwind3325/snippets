package org.clkg.scandemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ScanReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String str = intent.getStringExtra("barcode_string");
        if (str.charAt(0) == 65279) {
            str = str.substring(1) + "（带BOM）";
        }
        Log.i("ScanDemo", str);
        Toast.makeText(context, "扫描结果：" + str, Toast.LENGTH_LONG).show();
    }
}
