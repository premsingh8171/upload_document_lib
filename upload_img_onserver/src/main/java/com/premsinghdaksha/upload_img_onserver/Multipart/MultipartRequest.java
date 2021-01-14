package com.premsinghdaksha.upload_img_onserver.Multipart;

/**
 * Created by Admin on 29-03-2016.
 */

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;


public class MultipartRequest extends Request<String> {

// private MultipartEntity entity = new MultipartEntity();

    MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    HttpEntity httpentity;
    private static final String FILE_PART_NAME = "file";

    private final Response.Listener<String> mListener;

    public MultipartRequest(String url, Response.ErrorListener errorListener,
                            Response.Listener<String> listener,
                            Map<String, String> stringPart) throws UnsupportedEncodingException {
        super(Method.POST, url, errorListener);

        mListener = listener;
//buildMultipartEntity();

      try {

          if (stringPart != null) {
              for (Map.Entry<String, String> entry : stringPart.entrySet()) {
                  entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
              }
          }
          setRetryPolicy(new DefaultRetryPolicy(BaseServiceClient.REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy
                  .DEFAULT_BACKOFF_MULT));
      } catch (NullPointerException e) {
          e.printStackTrace();
      }

    }

    public void addImageData(String key, File file) {
        if (file != null) {
            entity.addBinaryBody(key, file, ContentType.create("image/jpeg"), file.getName());
        }
    }

    @Override
    public String getBodyContentType() {
        Log.d(getClass().getSimpleName(), "ContentType:" + httpentity.getContentType().getValue());
        return httpentity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpentity = entity.build();
            httpentity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        byte[] b = bos.toByteArray();

        Log.d(getClass().getSimpleName(), "Body: " + new String(b));
        return b;
    }


    @Override
    protected Response<String> parseNetworkResponse(com.android.volley.NetworkResponse response) {

        String parsedResponse = new String(response.data);
        Log.d(getClass().getSimpleName(), parsedResponse);

        return Response.success(parsedResponse, getCacheEntry());

    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}