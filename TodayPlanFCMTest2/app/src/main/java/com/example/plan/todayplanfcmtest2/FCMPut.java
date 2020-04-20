package com.example.plan.todayplanfcmtest2;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FCMPut extends StringRequest {
    final static private String URL = "http://13.125.73.140/todayplan/fcmtestBtoA.php";
    private Map<String, String> parameters;

    public FCMPut(String input, String id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("input", input);
        parameters.put("id", id);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}