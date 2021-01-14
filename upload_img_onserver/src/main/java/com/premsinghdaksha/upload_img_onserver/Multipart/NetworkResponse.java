package com.premsinghdaksha.upload_img_onserver.Multipart;

/**
 * Created by Admin on 29-03-2016.
 */
public class NetworkResponse {

    private static final int[] SERVER_SUCCESS_RESPONSE_CODE = {1001};

    public int status;

    public String statusMessage;

    public boolean isSuccessful() {

        boolean isSuccessful = false;

        for (int successCode : SERVER_SUCCESS_RESPONSE_CODE) {
            if (status == successCode) {
                isSuccessful = true;
                break;
            }
        }

        return isSuccessful;
    }
}
