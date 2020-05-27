package me.twodee.friendlyneighbor.service;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class VolleyUtils {

    public static void post(Context context, String url,
                            Map<Object, Object> data,
                            Response.Listener<JSONObject> responseListener,
                            Response.ErrorListener errorListener
    ) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonBody = new JSONObject(data);

        final String requestBody = jsonBody.toString();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url,
                                                                jsonBody, responseListener,
                                                                errorListener) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
            }

        };

        requestQueue.add(stringRequest);
    }
}
