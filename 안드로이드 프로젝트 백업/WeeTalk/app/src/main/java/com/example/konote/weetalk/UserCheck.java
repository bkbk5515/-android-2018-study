package com.example.konote.weetalk;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KONOTE on 2017-10-10.
 */

public class UserCheck extends StringRequest {
    final static private String URL = "http://bkbk55152.vps.phps.kr/usercheck.php";
    private Map<String, String> parameters;

    public UserCheck(String userMail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userMail", userMail);

    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}

