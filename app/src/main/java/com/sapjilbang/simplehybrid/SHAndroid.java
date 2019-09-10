package com.sapjilbang.simplehybrid;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;

public class SHAndroid {

    private SHWebView webView;

    SHAndroid(SHWebView webView) {
        this.webView = webView;
    }

    @JavascriptInterface
    public void init() {
        appDataToWeb();
    }

    @JavascriptInterface
    public void appDataToApp(String appData) {
        SharedPreferences sharedPreferences = webView.getContext().getSharedPreferences("SH", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("appData", appData);
        editor.commit();
    }

    @JavascriptInterface
    public void appDataToWeb() {
        final SharedPreferences sharedPreferences = webView.getContext().getSharedPreferences("SH", Context.MODE_PRIVATE);
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:shInitCallBack('" + sharedPreferences.getString("appData", "") + "')");
            }
        });
    }
}