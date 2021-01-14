package com.premsinghdaksha.upload_img_onserver.Multipart;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Admin on 29-03-2016.
 */
public abstract class BaseServiceClient {
//retrofit k leye use hota h
    public NetworkRequestType _networkRequestType;

    public static int REQUEST_TIMEOUT_MS = 60000;
    private Context _context;

    public BaseServiceClient(NetworkRequestType requestType) {
        this._networkRequestType = requestType;
    }

    public abstract void fireNetworkRequest(Context context, INetworkResponseHandler handler) throws
            ValidationException;

    public void fireNetworkRequest(Context context) throws ValidationException {
        this.fireNetworkRequest(context, null);

        this._context = context;
    }

    protected <T extends NetworkResponse> JsonObjectRequest createJsonObjectRequest(NetworkMethodType methodType,
                                                                                    String fullUrl, JSONObject
                                                                                            payLoad, final
                                                                                    INetworkResponseHandler handler,
                                                                                    final Type responseType) {
        Response.Listener listener = null;
        Response.ErrorListener errorListener = null;
        if (handler != null) {
            listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    T t = parseJsonResponse(response, responseType);
                    handler.onNetworkResponse(_networkRequestType, t);
                }
            };

            errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = new NetworkResponse();

                    networkResponse.statusMessage = error.getMessage();
                    Log.d("ERRR", error.networkResponse + "" + error.getMessage());

                    handler.onNetworkResponse(_networkRequestType, networkResponse);
                }
            };
        }

        Log.d(getClass().getSimpleName(), "Request URL: " + fullUrl);

        JsonObjectRequest request = new JsonObjectRequest(methodType.getVolleyMethodType(), fullUrl, payLoad,
                listener, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy
                        .DEFAULT_BACKOFF_MULT));

        return request;
    }

    protected void queueNetworkRequest(Context context, JsonObjectRequest jsonObjectRequest) {
        NetworkOperationHelper.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    protected <T> T parseJsonResponse(JSONObject response, Type type) {
        T result;
        Gson gson = new Gson();
        Log.d(getClass().getSimpleName(), "parseJsonRespnse: " + response.toString());
        result = gson.fromJson(response.toString(), type);
        return result;
    }



    protected JSONObject getJsonPayload(Object obj) throws JSONException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(obj);
        Log.d(getClass().getSimpleName(), "getJsonPayload: "+ jsonString);
        return new JSONObject(jsonString);
    }

    public static ImageLoader getImageLoader(Context context) {
        return NetworkOperationHelper.getInstance(context).getImageLoader();
    }

    public enum NetworkMethodType {
        Get, Post, Put;

        public int getVolleyMethodType() {
            int method;
            switch (this) {
                case Get:
                    method = Request.Method.GET;
                    break;
                case Post:
                    method = Request.Method.POST;
                    break;
                case Put:
                    method = Request.Method.PUT;
                    break;
                default:
                    method = Request.Method.GET;
                    break;
            }

            return method;
        }
    }

    public class ValidationException extends Exception {
        public ValidationException(String validationMessage) {
            this.validationErrorMessage = validationMessage;
        }

        public String validationErrorMessage;
    }

}