package my.com.kakao;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class Kakaoget1 extends StringRequest {
    final static private String URL = "http://13.125.73.140/oauth/payment.php";
    private Map<String, String> parameters;

    public Kakaoget1(String n, int p1, int p2, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();

        parameters.put("name", n);
        parameters.put("price", String.valueOf(p1));
        parameters.put("price1", String.valueOf(p2));
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}