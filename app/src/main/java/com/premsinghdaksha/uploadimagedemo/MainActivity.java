package com.premsinghdaksha.uploadimagedemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.premsinghdaksha.spots_progress_dialog.SpotsProgressDialog;
import com.premsinghdaksha.upload_img_onserver.uploadImage.UploadDocument;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ImageView img_upload;
    UploadDocument uploadDocument;
    public Context mContext;
    public String url_;
    public HashMap<String, String> map;
    public String file_key = "file";//iyour image key
    SpotsProgressDialog dialog;
    static final int REQUEST_IMAGE_CAPTURE = 101;
    ImageView pdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_upload = findViewById(R.id.img_upload);
        pdf = findViewById(R.id.pdf);
        img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCoverImage();
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPhotoMethod(v);
            }
        });
    }


    private void selectCoverImage() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.OFF).start(MainActivity.this);
    }

    public void SelectPhotoMethod(View view) {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select document"), REQUEST_IMAGE_CAPTURE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {
                    Uri resulturi = CropImage.getActivityResult(data).getUri();
                    File file = new File(resulturi.getPath());
                    //resultUr = result.getUri();
                    String image = resulturi.getPath();
                    Log.d("image__", image);
                    uploadDocument(file);

                    //if you want show image before uploading then use
//                    Glide.with(MainActivity.this).load(file)
//                            .placeholder(R.drawable.uploadimg)
//                            .error(R.drawable.uploadimg)
//                            .into(img_upload);
                }
            }

        } catch (Exception e) {
        }

//        if (REQUEST_IMAGE_CAPTURE == 101) {
//            try {
//                Uri uri = data.getData();
//                File file = FileUtil.from(MainActivity.this, uri);
//                Log.d("file__", "File...:::: uti - " + file.getPath() + " file -" + file + " : " + file.exists());
//
//                uploadDocument(file);
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }


    }

    private void uploadDocument(File file) {
        dialog = (SpotsProgressDialog) new SpotsProgressDialog.Builder()
                .setContext(MainActivity.this)
                .setMessage("Please wait, processing...")
                .setCancelable(false)
                .build();
        dialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile_no", "9267909750");
        //map.put("KEY", "VALUE");
        //  map.put("KEY", "VALUE");
        //ETC.

        String url = "http://institutepartner.com/public/api/upload";
        file_key = "image";

        // file_key your image upload key variable like file, image etc.

        uploadDocument = new UploadDocument(MainActivity.this, file_key, file, url, map, new UploadDocument.OnResponce() {
            @Override
            public void onSuccess(String responce) {
                dialog.dismiss();
                Log.d("responce__r", responce);
                try {
                    JSONObject obj = new JSONObject(responce);
                    JSONObject dataObj = obj.optJSONObject("data");
                    JSONObject resultsObj = dataObj.optJSONObject("results");
                    String message = obj.optString("message");
                    String image = resultsObj.optString("image");
                    Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_LONG);

                    Glide.with(MainActivity.this).load(image)
                            .placeholder(R.drawable.uploadimg)
                            .error(R.drawable.uploadimg)
                            .into(img_upload);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                dialog.dismiss();
                Log.d("error_", String.valueOf(error));

            }
        });

    }


}