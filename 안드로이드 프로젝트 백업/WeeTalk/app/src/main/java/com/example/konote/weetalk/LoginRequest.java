package com.example.konote.weetalk;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KONOTE on 2017-10-11.
 */

public class LoginRequest extends StringRequest {
    final static private String URL = "http://bkbk55152.vps.phps.kr/userlogin.php";
    private Map<String, String> parameters;

    public LoginRequest(String userMail, String userPassword, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userMail", userMail);
        parameters.put("userPassword", userPassword);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
