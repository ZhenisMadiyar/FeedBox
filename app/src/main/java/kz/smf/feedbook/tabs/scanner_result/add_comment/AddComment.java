package kz.smf.feedbook.tabs.scanner_result.add_comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by madiyarzhenis on 13.10.15.
 */
public class AddComment extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;
    Button sendBtn;
    EditText title;
    EditText content;
    AsyncHttpClient client = new AsyncHttpClient();
    AsyncHttpClient clinetUpload = new AsyncHttpClient();
    Spinner spinner;
    File imageFile = null;
    String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(kz.smf.feedbook.R.layout.add_comment);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Add comment");

        spinner = (Spinner) findViewById(kz.smf.feedbook.R.id.spinner);
        title = (EditText) findViewById(kz.smf.feedbook.R.id.editTextTitle);
        content = (EditText) findViewById(kz.smf.feedbook.R.id.editTextCommentText);
        sendBtn = (Button) findViewById(kz.smf.feedbook.R.id.buttonSend);
        imageView = (ImageView) findViewById(kz.smf.feedbook.R.id.imageViewComment);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                kz.smf.feedbook.R.array.time, android.R.layout.simple_spinner_item);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Log.i("Selected", item.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageFile != null && !title.getText().toString().equals("") && !content.getText().toString().equals("")) {

                    RequestParams requestParamsImage = new RequestParams();
//                    File fileImage = new File("/storage/emulated/0/DCIM/Camera/1444969025833.jpg");
                    try {
                        requestParamsImage.put("file", imageFile);
                        requestParamsImage.put("userid", String.valueOf(1));
                        Log.i("imageRequest", requestParamsImage.toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    clinetUpload.post(AddComment.this, "http://88.198.48.246:9017/image/upload", requestParamsImage, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.e("statusCode", statusCode + "");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            Log.i("statusCodeImage", statusCode + "");
                            Log.i("ResponseImage", responseString);
                            try {
                                JSONObject jsonImage = new JSONObject(responseString);
                                JSONArray jsonImageArray = jsonImage.getJSONArray("params");
                                JSONObject jsonObjectImageParams = jsonImageArray.getJSONObject(0);
                                JSONObject jsonValue = jsonObjectImageParams.getJSONObject("value");
                                code = jsonValue.getString("code");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONObject jsonCommand = new JSONObject();
                            JSONObject jsonParams = new JSONObject();
                            JSONArray jsonImg = new JSONArray();
                            RequestParams requestParams = new RequestParams();
                            try {
                                jsonCommand.put("command", "postFeedback");
                                jsonCommand.put("params", jsonParams);
                                jsonParams.put("descr", content.getText().toString());
                                jsonParams.put("title", title.getText().toString());
                                jsonParams.put("timeToFix", spinner.getSelectedItem());
                                jsonParams.put("imgs", jsonImg);
                                jsonParams.put("place_code", 2);
                                jsonImg.put(code);
                                requestParams.add("userid", String.valueOf(1));
                                requestParams.add("data", jsonCommand.toString());
                                Log.i("JsonStr", jsonCommand.toString());
                                Log.i("Request", requestParams.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            client.post(AddComment.this, "http://88.198.48.246:9017/app/run", requestParams, new TextHttpResponseHandler() {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    Log.e("statusCode", statusCode + "");
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                    Log.i("statusCode", statusCode + "");
                                    Log.i("Response", responseString);
                                }
                            });
                        }
                    });
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter title or description", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            Uri tempUri = getImageUri(getApplicationContext(), photo);

            File finalFile = new File(getRealPathFromURI(tempUri));

            imageFile = finalFile;
            Log.i("ImageFilePath", finalFile.toString());
//            System.out.println(finalFile);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
