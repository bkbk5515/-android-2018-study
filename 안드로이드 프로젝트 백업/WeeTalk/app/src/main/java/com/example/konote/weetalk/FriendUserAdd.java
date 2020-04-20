package com.example.konote.weetalk;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KONOTE on 2017-12-05.
 */
public class FriendUserAdd extends StringRequest {
    final static private String URL = "http://bkbk55152.vps.phps.kr/frienduserget.php";
    private Map<String, String> parameters;

    public FriendUserAdd(String name, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("CheakName", name);
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}