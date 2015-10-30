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
import kz.smf.almaty.feedbook.R;

/**
 * Created by madiyarzhenis on 14.10.15.
 */
public class RegisterActivity extends Activity {
    EditText editTextPhone;
    Button nextBtn;
    private static AsyncHttpClient client = new AsyncHttpClient();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);

        editTextPhone = (EditText) findViewById(R.id.editTextPhoneNumber);
        nextBtn = (Button) findViewById(R.id.buttonNext);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPhone.getText().toString() != null) {
                    JSONObject jsonCommand = new JSONObject();
                    JSONObject jsonParams = new JSONObject();
                    RequestParams requestParams = new RequestParams();
                    try {
                        jsonCommand.put("command", "sendSmsCode");
                        jsonCommand.put("params", jsonParams);
                        jsonParams.put("phone", "7"+editTextPhone.getText().toString());
                        requestParams.add("data", jsonCommand.toString());
                        Log.i("JsonStr", jsonCommand.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    client.post(RegisterActivity.this, "http://88.198.48.246:9017/app/run", requestParams, new TextHttpResponseHandler() {
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
                    Intent intent = new Intent(RegisterActivity.this, VerifyCode.class);
                    intent.putExtra("phoneNumber", editTextPhone.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                }
            }
        });
    }
}
