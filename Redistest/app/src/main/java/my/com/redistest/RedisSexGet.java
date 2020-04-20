package my.com.redistest;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RedisSexGet extends StringRequest {
    final static private String URL = "http://13.125.73.140/redissexget.php";
    private Map<String, String> parameters;

    public RedisSexGet(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}