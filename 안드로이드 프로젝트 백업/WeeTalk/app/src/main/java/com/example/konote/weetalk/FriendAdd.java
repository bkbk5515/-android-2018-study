package com.example.konote.weetalk;

/**
 * Created by KONOTE on 2017-12-04.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class FriendAdd extends StringRequest {
    final static private String URL = "http://bkbk55152.vps.phps.kr/friendadd2.php";
    private Map<String, String> parameters;

    public FriendAdd(String _myname, String _friendname, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("myname", _myname);
        parameters.put("friendname", _friendname);
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
