package com.donglai.sm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    WebView webView;
    BroadcastReceiver receiver;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        webView.setOnLongClickListener(v -> true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        webView.addJavascriptInterface(this, "android");
        webView.loadUrl("https://ss-test.onwings.com:8050/mobile/#/");
        setContentView(webView);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String str = intent.getStringExtra("barcode_string");
                if (str.charAt(0) == 65279) {
                    str = str.substring(1);
                }
                webView.evaluateJavascript("javascript:window.$scan('" + str + "');", s -> {
                });
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.ACTION_DECODE_DATA");
        registerReceiver(receiver, intentFilter);

        tts = new TextToSpeech(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            String url = webView.getUrl();
            if (!url.endsWith("#/")) {
                webView.evaluateJavascript("javascript:history.back();", s -> {
                });
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @JavascriptInterface
    public String invoke(String action, String param) {
        switch (action) {
            case "speak":
                speak(param);
                return "";
            default:
                return "";
        }
    }

    @Override
    public void onInit(int status) {
        tts.setLanguage(Locale.CHINA);
        tts.setPitch(1.0f);
        tts.setSpeechRate(1.0f);
    }

    public void speak(String str) {
        if (tts == null) return;
        tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }
}