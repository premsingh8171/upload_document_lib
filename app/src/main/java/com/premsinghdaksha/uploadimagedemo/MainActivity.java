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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_upload = findViewById(R.id.img_upload);
        img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCoverImage();
            }
        });
    }


    private void selectCoverImage() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.OFF).start(MainActivity.this);
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
                    Glide.with(MainActivity.this).load(file)
                            .placeholder(R.drawable.uploadimg)
                            .error(R.drawable.uploadimg)
                            .into(img_upload);
                }
            }

        } catch (Exception e) {
        }
    }

    private void uploadDocument(File file) {
        dialog = (SpotsProgressDialog) new SpotsProgressDialog.Builder()
                .setContext(MainActivity.this)
                .setMessage("Please wait, processing...")
                .setCancelable(false)
                .build();
        dialog.show();
        map = new HashMap<>();
        map.put("KEY", "VALUE");
        map.put("KEY", "VALUE");
        map.put("KEY", "VALUE");
        //ETC

        String url = "YOUR URL";

        // file_key your image upload key variable like file, image etc.

        uploadDocument = new UploadDocument(MainActivity.this, file_key, file, url, map, new UploadDocument.OnResponce() {
            @Override
            public void onSuccess(String responce) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "" + responce, Toast.LENGTH_LONG);
            }

            @Override
            public void onError(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG);

            }
        });

    }


}