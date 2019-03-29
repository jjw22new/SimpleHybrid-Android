package com.sapjilbang.simplehybrid;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;

import java.util.HashMap;
import java.util.Map;

public class SH {

    public static class data {

        Context mContext;
        static Map<String, String> data;

        data(Context c) {
            mContext = c;

            data = new HashMap<>();
        }

        @JavascriptInterface
        public static String get(String name) {
            return data.get(name);
        }

        @JavascriptInterface
        public static void set(String name, String value) {
            data.put(name, value);
        }

        @JavascriptInterface
        public static void clear() {
            data.clear();
        }

        public int size() {
            return data.size();
        }

        @JavascriptInterface
        public String count() {
            return String.valueOf(data.size());
        }

        public boolean containsKey(String name) {
            return data.containsKey(name);
        }

        @JavascriptInterface
        public String hasOwnProperty(String name) {
            if(data.containsKey(name)) {
                return "true";
            } else {
                return "false";
            }
        }

        @JavascriptInterface
        public static void remove(String name) {
            data.remove(name);
        }

        @JavascriptInterface
        public String load(String name) {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("data.load", Context.MODE_PRIVATE);
            return sharedPreferences.getString(name, "");
        }

        @JavascriptInterface
        public void save(String name, String value) {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("data.save", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(name, value);
            editor.commit();
        }
    }
}
