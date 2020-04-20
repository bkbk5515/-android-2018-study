package com.example.plan.todayplanfcmtest;


import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMPut extends StringRequest {
    final static private String URL = "http://13.125.73.140/todayplan/fcmtestAtoB.php";
    private Map<String, String> parameters;

    String title, contents;

    //json 추가할때 throws 추가됨
    public FCMPut(String input,String id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        Log.d("받은input", input);

        parameters.put("input", input);
        parameters.put("id", id);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}