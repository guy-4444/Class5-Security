package com.guy.class5_security;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyApis {

    public interface CallBack_Response {
        void CallBack_ResponseReturned(boolean isSuccess, String response);
    }

    public static void callAPI(Context context, String API, final CallBack_Response callBack_response) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject parameters = new JSONObject();

        StringRequest rq = new StringRequest(Request.Method.GET
                , API
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("pttt", "Response: " + response);
                callBack_response.CallBack_ResponseReturned(true, response);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("pttt", "Error: " + error.toString());
                callBack_response.CallBack_ResponseReturned(false, error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                //Log.d("pttt", "Token: " + authResult.getIdToken().toString());
                //headers.put("Authorization", "Bearer " + authResult.getIdToken());
                headers.put("content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        rq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(rq);
    }
}
