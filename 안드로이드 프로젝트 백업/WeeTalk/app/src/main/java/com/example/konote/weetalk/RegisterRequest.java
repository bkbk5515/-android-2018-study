package com.example.konote.weetalk;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KONOTE on 2017-10-10.
 */

public class RegisterRequest extends StringRequest {
    final static private String URL = "http://bkbk55152.vps.phps.kr/userregister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userMail, String userPassword, String userPhonenumber, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userMail", userMail);
        parameters.put("userPassword", userPassword);
        parameters.put("userPhonenumber", userPhonenumber);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
