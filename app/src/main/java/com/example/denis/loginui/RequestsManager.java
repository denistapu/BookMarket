package com.example.denis.loginui;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class RequestsManager {

    final String URL = "http://transfertk.ddns.net:8063/api/Api.php";
    RequestQueue mQueue;
    JsonObjectRequest request;

    public RequestsManager(Context _context){
        mQueue = Volley.newRequestQueue(_context);
    }

    public void execRequest(String type, Map<String,String> params, Response.Listener<JSONObject> response){
        String reqUrl = URL+ "?request="+type;
        for(Map.Entry<String,String> entry : params.entrySet())
            reqUrl += "&"+entry.getKey()+"="+entry.getValue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, reqUrl, null,response, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
    }
}