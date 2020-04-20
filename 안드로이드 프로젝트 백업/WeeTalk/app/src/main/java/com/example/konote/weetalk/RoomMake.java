package com.example.konote.weetalk;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KONOTE on 2017-11-20.
 */

public class RoomMake extends StringRequest {
    final static private String URL = "http://bkbk55152.vps.phps.kr/roommake.php";
    private Map<String, String> parameters;

    public RoomMake(String _roomname, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("roomname", _roomname);

    }



    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
