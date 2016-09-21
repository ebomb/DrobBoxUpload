package com.demo.dropboxupload.utils;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Eli on 9/20/2016.
 * A handy utility to help with JSON functions.
 */

public class JSONHelper {

    public static Map<String, Object> JSONToMap(JSONObject object) {
        Map<String, Object> map = new HashMap<>();

        try {
            Iterator<String> keysItr = object.keys();

            while (keysItr.hasNext()) {
                String key = keysItr.next();
                Object value = object.get(key);

                if (value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                } else if (value instanceof JSONObject) {
                    value = JSONToMap((JSONObject) value);
                }

                map.put(key, value);
            }
        } catch (Exception ex) {
            return null;
        }

        return map;
    }


    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = JSONToMap((JSONObject) value);
            }

            list.add(value);
        }

        return list;
    }

    public static String getString(JSONObject obj, String key) {
        String val = null;
        if (containsKey(obj, key)) {
            try {
                val = obj.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    public static long getLong(JSONObject obj, String key) {
        long val = 0;
        if (containsKey(obj, key)) {
            try {
                val = obj.getLong(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    public static float getFloat(JSONObject obj, String key) {
        float val = 0;
        if (containsKey(obj, key)) {
            try {
                val = Float.parseFloat(obj.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    public static double getDouble(JSONObject obj, String key) {
        double val = 0;
        if (containsKey(obj, key)) {
            try {
                val = obj.getDouble(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    public static boolean getBoolean(JSONObject obj, String key) {
        boolean val = false;
        if (containsKey(obj, key)) {
            try {
                val = obj.getBoolean(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    public static JSONObject getJSONObject(JSONObject obj, String key) {
        JSONObject val = null;
        if (containsKey(obj, key)) {
            try {
                val = obj.getJSONObject(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    public static int getInt(JSONObject obj, String key) {
        int val = 0;
        if (containsKey(obj, key)) {
            try {
                val = obj.getInt(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    public static JSONArray getJSONArray(JSONObject obj, String key) {
        JSONArray val = null;
        if (containsKey(obj, key)) {
            try {
                val = obj.getJSONArray(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    public static boolean containsKey(JSONObject obj, String key) {
        return obj != null && obj.has(key);
    }

    public static void put(JSONObject obj, String key, boolean value) {
        if (obj != null && !TextUtils.isEmpty(key)) {
            try {
                obj.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void put(JSONObject obj, String key, String value) {
        if (obj != null && !TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            try {
                obj.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void put(JSONObject obj, String key, double value) {
        if (obj != null && !TextUtils.isEmpty(key)) {
            try {
                obj.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONObject convertStringToJSONObject(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public static JSONObject getJSONResource(Context mContext, int resourceID) {
        InputStream is = mContext.getResources().openRawResource(resourceID);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return new JSONObject(writer.toString());
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}