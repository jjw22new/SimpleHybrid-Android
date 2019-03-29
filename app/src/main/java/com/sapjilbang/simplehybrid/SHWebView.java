package com.sapjilbang.simplehybrid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SHWebView extends WebView {

    /**
     * WebView 의 {@link #setOnScrollChangeListener(View.OnScrollChangeListener) (API 23)} 을 지원하기 위한 interface
     */
    public interface OnScrollChangeListener {
        /**
         * Called when the scroll position of a view changes.
         *
         * @param v          The view whose scroll position has changed.
         * @param scrollX    Current horizontal scroll origin.
         * @param scrollY    Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        void onScrollChange(WebView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    private List<OnScrollChangeListener> onScrollChangeListeners = new ArrayList<>();

    public SHWebView(Context context) {
        super(context);

        init();
    }

    public SHWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public SHWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        for (OnScrollChangeListener onScrollChangeListener : onScrollChangeListeners) {
            onScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    /**
     * @param onScrollChangeListener
     */
    public void addOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        onScrollChangeListeners.add(onScrollChangeListener);
    }

    /**
     * @param onScrollChangeListener
     */
    public void removeOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        onScrollChangeListeners.remove(onScrollChangeListener);
    }

    public void init() {
        WebSettings settings = this.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        File dir = this.getContext().getCacheDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        settings.setAppCachePath(dir.getPath());
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);

        //Webview UserAgent
        String userAgent = settings.getUserAgentString();
        if (!userAgent.contains("SimpleHybrid")) {
            userAgent = String.format("%s;%s;", settings.getUserAgentString(), "SimpleHybrid");
            settings.setUserAgentString(userAgent);
        }

        //Add Interface
        this.addJavascriptInterface(new SH.data(this.getContext()), "SH.data");

        //WebViewClient
        /*
        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(shouldOverrideUrlLoadingCommon(view, url)) {
                    return false;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(shouldOverrideUrlLoadingCommon(view, request.getUrl().toString())) {
                    return false;
                } else {
                    return super.shouldOverrideUrlLoading(view, request);
                }
            }

            public boolean shouldOverrideUrlLoadingCommon(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if(url.startsWith("SH://")) {
                    if(url.startsWith("SH://View")) {
                        if(uri.getQueryParameter("data") != null) {
                            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("View.Data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(uri.getQueryParameter("name"), uri.getQueryParameter("data"));
                            editor.commit();
                        } else {
                            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("View.Data", Context.MODE_PRIVATE);
                            sharedPreferences.getString(uri.getQueryParameter("name"), "");
                            view.loadUrl("javascript:");
                        }
                    }
                    return true;
                } else {
                    return false;
                }
                return true;
            }
        });
        */

        //WebChromeClient
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(message)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                result.cancel();
                            }
                        })
                        .show();
                //TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                //textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(message)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                result.cancel();
                            }
                        })
                        .show();
                //TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                //textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                return true;
            }
        });
    }
}