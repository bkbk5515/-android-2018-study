package my.com.kakaoblog;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class kakaocafeget extends StringRequest {
    final static private String URL = "http://13.125.73.140/oauth/kakaoblog.php";
    private Map<String, String> parameters;

    public kakaocafeget(String n, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("name", n);
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}