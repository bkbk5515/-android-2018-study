package my.com.redistest;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class LiveStart extends StringRequest {
    final static private String URL = "http://13.125.73.140/rediscount.php";
    private Map<String, String> parameters;

    public LiveStart(String filename, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        Log.d("전달받은 filename", filename);

        parameters.put("filename", filename);

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}