package su.sorex.com.sorex_front_app.Utils;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.fragment.app.Fragment;
import su.sorex.com.sorex_front_app.VolleyCallBacks;

public class HTTPRequestHandler {

    public static String BASE_URL = "http://ec2-52-15-185-107.us-east-2.compute.amazonaws.com:8080";
//    public static String BASE_URL = "http://192.168.43.173:8080";
//    public static String BASE_URL = "http://10.0.0.2:8080";
    private static RequestQueue requestQueue;
    private static Context _ctx;

    public static void init(Context ctx) {
        _ctx = ctx;
        requestQueue = Volley.newRequestQueue(ctx);
    }

    public static int createWallet(String password, final VolleyCallBacks caller) {
        String url = BASE_URL + "/wallet/create?password="+password;
        final int resp;
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        textView.setText("Response is: "+ response.substring(0,500));
                        Toast.makeText(_ctx, "Resoinse good", Toast.LENGTH_SHORT).show();

                        caller.volleyResponse(response); // This will make a callback to activity.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
                System.out.println(error.toString());
            }
        });
        requestQueue.add(stringRequest);
        return -1;
    }

}
