package com.example.konote.weetalk;

/**
 * Created by KONOTE on 2017-12-04.
 */
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RoomListAdd extends StringRequest {
    final static private String URL = "http://bkbk55152.vps.phps.kr/room.php";
    private Map<String, String> parameters;

    public RoomListAdd(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
