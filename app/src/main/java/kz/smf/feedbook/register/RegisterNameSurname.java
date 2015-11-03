package kz.smf.feedbook.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import kz.smf.feedbook.R;
import kz.smf.feedbook.RegisterUtils;

/**
 * Created by madiyarzhenis on 14.10.15.
 */
public class RegisterNameSurname extends Activity {
    EditText editTextName;
    Button btnNext;
    private static AsyncHttpClient client = new AsyncHttpClient();
    String phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_name);
        editTextName = (EditText) findViewById(R.id.editTextName);
        btnNext = (Button) findViewById(R.id.buttonNextName);

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.getText() != null){
                    JSONObject jsonCommand = new JSONObject();
                    JSONObject jsonParams = new JSONObject();
                    RequestParams requestParams = new RequestParams();
                    try {
                        jsonCommand.put("command", "register");
                        jsonCommand.put("params", jsonParams);

                        jsonParams.put("name", editTextName.getText().toString());
                        jsonParams.put("phone", "7" + phone);

                        requestParams.add("data", jsonCommand.toString());
                        Log.i("JsonStr", jsonCommand.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    client.post(RegisterNameSurname.this, "http://88.198.48.246:9017/app/run", requestParams, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.e("statusCode", statusCode + "");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            Log.i("statusCode", statusCode + "");
                            Log.i("Response", responseString);
                            try {
                                JSONObject jsonObject = new JSONObject(responseString);
                                if (jsonObject.getInt("status") == 200) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                                    JSONObject jsonUser = jsonArray.getJSONObject(0);

                                    RegisterUtils.setDefaults(RegisterNameSurname.this, jsonUser.getString("name"), jsonUser.getString("phone"),
                                            jsonUser.getString("privateKey"), jsonUser.getInt("identifier"), jsonUser.getString("userCode"));

                                    Intent intentName = new Intent(RegisterNameSurname.this, AccessPushNotification.class);
                                    startActivity(intentName);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                } else {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }
}
