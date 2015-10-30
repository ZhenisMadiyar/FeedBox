package kz.smf.almaty.feedbook.register;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import kz.smf.almaty.feedbook.R;
import kz.smf.almaty.feedbook.TabsActivity;

/**
 * Created by madiyarzhenis on 30.10.15.
 */
public class AccessPushNotification extends Activity {

    Button enable, disable;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "1047520583750";
    AsyncHttpClient client = new AsyncHttpClient();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access_notification);

        enable = (Button) findViewById(R.id.buttonEnable);
        disable = (Button) findViewById(R.id.buttonDisable);

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRegId();
                JSONObject jsonCommand = new JSONObject();
                JSONObject jsonParams = new JSONObject();
                RequestParams requestParams = new RequestParams();
                try {
                    jsonCommand.put("command", "submitDevice");
                    jsonCommand.put("params", jsonParams);
                    jsonParams.put("source", "android");
                    jsonParams.put("token", regid);
                    requestParams.add("data", jsonCommand.toString());
                    requestParams.add("userid", String.valueOf(1));
                    Log.i("JsonStr", jsonCommand.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                client.post(AccessPushNotification.this, "http://88.198.48.246:9017/app/run", requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("statusCodePush", statusCode + "");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("statusCodePush", statusCode + "");
                        Log.i("ResponsePush", responseString);
                        Intent intentName = new Intent(AccessPushNotification.this, TabsActivity.class);
                        startActivity(intentName);
                    }
                });
            }
        });


        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentName = new Intent(AccessPushNotification.this, TabsActivity.class);
                startActivity(intentName);
            }
        });
    }
    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM", msg);


                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                etRegId.setText(msg + "\n");
                Log.i("RegId", msg);
            }
        }.execute(null, null, null);
    }
}
