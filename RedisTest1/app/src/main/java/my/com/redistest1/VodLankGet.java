package my.com.redistest1;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class VodLankGet extends StringRequest {
    final static private String URL = "http://13.125.73.140/redisvodcountget.php";
    private Map<String, String> parameters;

    public VodLankGet(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}