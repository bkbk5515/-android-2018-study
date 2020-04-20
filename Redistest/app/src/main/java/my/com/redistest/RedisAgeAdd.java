package my.com.redistest;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class RedisAgeAdd extends StringRequest {
    final static private String URL = "http://13.125.73.140/redisageadd.php";
    private Map<String, String> parameters;

    public RedisAgeAdd(String age, String id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        Log.d("전달받은 age", age);
        Log.d("전달받은 id", id);

        parameters.put("age", age);
        parameters.put("id", id);

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}