package com.peky.irrigation_app.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    // Convert a string to a JSONObject
    public static JSONObject stringToJsonObject(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Convert a JSONObject to a string
    public static String jsonObjectToString(JSONObject jsonObject) {
        return jsonObject.toString();
    }

    // Convert a string to a JSONArray
    public static JSONArray stringToJsonArray(String jsonString) {
        try {
            return new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Convert a JSONArray to a string
    public static String jsonArrayToString(JSONArray jsonArray) {
        return jsonArray.toString();
    }

    // Get a string value from a JSONObject
    public static String getStringFromJsonObject(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get an int value from a JSONObject
    public static int getIntFromJsonObject(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Get a boolean value from a JSONObject
    public static boolean getBooleanFromJsonObject(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add a string to a JSONObject
    public static void putStringToJsonObject(JSONObject jsonObject, String key, String value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Add an int to a JSONObject
    public static void putIntToJsonObject(JSONObject jsonObject, String key, int value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Add a boolean to a JSONObject
    public static void putBooleanToJsonObject(JSONObject jsonObject, String key, boolean value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Get a JSONArray from a JSONObject
    public static JSONArray getJsonArrayFromJsonObject(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}