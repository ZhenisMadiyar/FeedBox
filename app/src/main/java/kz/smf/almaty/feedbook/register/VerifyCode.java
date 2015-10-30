package kz.smf.almaty.feedbook.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by madiyarzhenis on 14.10.15.
 */
public class VerifyCode extends Activity {
    Button next;
    EditText editTextCode;
    private static AsyncHttpClient client = new AsyncHttpClient();
    String phoneNumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(kz.smf.almaty.feedbook.R.layout.verify_code);

        final Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        next = (Button) findViewById(kz.smf.almaty.feedbook.R.id.buttonNextCode);
        editTextCode = (EditText) findViewById(kz.smf.almaty.feedbook.R.id.editTextVerifyCode);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCode.getText().toString() != null) {
                    JSONObject jsonCommand = new JSONObject();
                    JSONObject jsonParams = new JSONObject();
                    RequestParams requestParams = new RequestParams();
                    try {
                        jsonCommand.put("command", "checkSmsCode");
                        jsonCommand.put("params", jsonParams);
                        jsonParams.put("phone", "7"+phoneNumber);
                        jsonParams.put("code", editTextCode.getText().toString());
                        requestParams.add("data", jsonCommand.toString());
                        Log.i("JsonStr", jsonCommand.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    client.post(VerifyCode.this, "http://88.198.48.246:9017/app/run", requestParams, new TextHttpResponseHandler() {
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
                                    Intent intentName = new Intent(VerifyCode.this, RegisterNameSurname.class);
                                    intentName.putExtra("phone", phoneNumber);
                                    startActivity(intentName);
                                    overridePendingTransition(kz.smf.almaty.feedbook.R.anim.left_to_right, kz.smf.almaty.feedbook.R.anim.right_to_left);
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
