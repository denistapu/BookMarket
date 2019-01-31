package com.example.denis.loginui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class RequestsManager {


    //final String URL = "http://transfertk.ddns.net:8063/api/Api.php";
    final String URL = "http://bookmarket.x10.mx/api/Api.php";
    RequestQueue mQueue;
    JsonObjectRequest request;
    Context context;

    public RequestsManager(Context _context){
        context = _context;
        mQueue = Volley.newRequestQueue(_context);
    }

    public void execRequest(String type, Map<String,String> params, Response.Listener<String> response){
        params.put("request",type);
        final Map<String,String> dParams = params;
        StringRequest request = new StringRequest(Request.Method.POST, URL,response, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("gx8", "Failed with error msg:\t" + error.getMessage());
                Log.d("gx8", "Error StackTrace: \t" + error.getStackTrace());
                // edited here
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    Log.e("gx8", new String(htmlBodyBytes), error);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (error.getMessage() == null){
                    //createUser();
                }
            }
        }){
            protected Map<String, String> getParams() {
               return dParams;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
    }
}
