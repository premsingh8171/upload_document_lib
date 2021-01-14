package com.premsinghdaksha.upload_img_onserver.Multipart;

/**
 * Created by Admin on 29-03-2016.
 */
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


public class NetworkOperationHelper {

    private static final String TAG = "NetworkOperationHelper";

    private static NetworkOperationHelper _sNetworkOperationHelper;
    private final Context _context;
    private RequestQueue _requestQueue;
    private ImageLoader _imageLoader;

    private NetworkOperationHelper(Context context) {
        _context = context;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        if (req == null) {
            Log.d(TAG, "addToRequestQueue: missing required Request object.");
            return;
        }

        getRequestQueue().add(req);
    }

    public void cancelNetworkRequestsForTag(Object... tags) {
        if (tags == null) {
            return;
        }

        for (Object tag : tags) {
            getRequestQueue().cancelAll(tag);
        }
    }

    public ImageLoader getImageLoader() {
        if (_imageLoader == null) {
            _imageLoader = new ImageLoader(getRequestQueue(),new LruBitmapCache());
        }
        return _imageLoader;
    }

    private RequestQueue getRequestQueue() {
        if (_requestQueue == null) {
            _requestQueue = Volley.newRequestQueue(_context);
        }

        return _requestQueue;
    }

    public static synchronized NetworkOperationHelper getInstance(Context context) {
        if (_sNetworkOperationHelper == null && context == null) {
            Log.d(TAG, "getInstance: missing Context object");
            return null;
        }

        if (_sNetworkOperationHelper == null) {
            _sNetworkOperationHelper = new NetworkOperationHelper(context.getApplicationContext());
        }

        return _sNetworkOperationHelper;
    }
}