package com.premsinghdaksha.upload_img_onserver.uploadImage;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.premsinghdaksha.upload_img_onserver.Multipart.MultipartRequest;
import com.premsinghdaksha.upload_img_onserver.Multipart.NetworkOperationHelper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class UploadDocument {
    public Context mContext;
    public File file;
    public String url;
    public HashMap<String, String> map;
    public String file_key;
    public OnResponce responce;

    public UploadDocument(Context mContext, String file_key, File file, String url, HashMap<String, String> map, OnResponce responce) {
        this.mContext = mContext;
        this.file = file;
        this.url = url;
        this.file_key = file_key;
        this.map = map;
        this.responce = responce;
        uploadDocument();

    }

    private void uploadDocument() {
       // Log.d("map___", String.valueOf(map));
        MultipartRequest req = null;
        try {
            req = new MultipartRequest(url, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    responce.onError(error);
                    return;
                }
            }, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    responce.onSuccess(response);

                }
            }, map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        req.addImageData(file_key, file);
        NetworkOperationHelper.getInstance(mContext).addToRequestQueue(req);
        req.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
    }

    public interface OnResponce {
        public void onSuccess(String responce);

        public void onError(VolleyError error);
    }

}
