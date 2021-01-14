# upload_document_lib

Android Multipart with moving uploat document packed as android library.

<img src="https://github.com/premsingh8171/upload_document_lib/blob/master/app/src/main/res/drawable/gifimg_.gif" width="300" height="550" /> <img src="https://github.com/premsingh8171/upload_document_lib/blob/master/app/src/main/res/drawable/upload1.jpeg" width="300" height="550" /> <img src="https://github.com/premsingh8171/upload_document_lib/blob/master/app/src/main/res/drawable/upload2.jpeg" width="300" height="550" /> <img src="https://github.com/premsingh8171/upload_document_lib/blob/master/app/src/main/res/drawable/upload3.jpeg" width="300" height="550" /> <img src="https://github.com/premsingh8171/upload_document_lib/blob/master/app/src/main/res/drawable/upload4.jpeg" width="300" height="550" /> <img src="https://github.com/premsingh8171/upload_document_lib/blob/master/app/src/main/res/drawable/upload5.jpeg" width="300" height="550" /> 







### Usage

The library available in maven jitpack repository. You can get it using:
```dependency
repositories {

  		 maven { url 'https://jitpack.io' }
}

```

```
dependencies {
    
    implementation 'com.github.premsingh8171:upload_document_lib:1.0'
}
```
## Useing

```java
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
    }
```

```java
Define variable

    public String url_;
    public HashMap<String, String> map;
    public String file_key = "file";//iyour image key
```

```java
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

```


<h2>Created and maintained by:</h2>
<p>Er.Prem singh daksha  premsingh8171@gmail.com</p>
<p><a href="https://www.linkedin.com/in/prem-singh-daksha-82az/"> <img src="https://github.com/anitaa1990/DeviceInfo-Sample/blob/master/media/linkedin-icon.png" alt="Linkedin" style="max-width:100%;"> </a></p>
