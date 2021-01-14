package com.premsinghdaksha.upload_img_onserver.Multipart;



/**
 * Created by Admin on 29-03-2016.
 */
public interface INetworkResponseHandler<T> {

    void onNetworkResponse(NetworkRequestType type, NetworkResponse response);
}
