package kz.smf.feedbook.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import kz.smf.feedbook.R;
import kz.smf.feedbook.RegisterUtils;
import kz.smf.feedbook.register.RegisterActivity;

/**
 * Created by madiyarzhenis on 31.08.15.
 */
public class Settings extends Fragment {
    Button deleteBtn;
    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        deleteBtn = (Button) view.findViewById(R.id.buttonDelete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Clicked!", Toast.LENGTH_SHORT).show();
                JSONObject jsonCommand = new JSONObject();
                JSONObject jsonParams = new JSONObject();
                RequestParams requestParams = new RequestParams();
                try {
                    jsonCommand.put("command", "deleteUser");
                    jsonCommand.put("params", jsonParams);
                    requestParams.add("data", jsonCommand.toString());
                    requestParams.add("userid", String.valueOf(RegisterUtils.getUserId(getActivity(), RegisterUtils.TAG_UserId)));
                    Log.i("JsonStr", requestParams.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                client.post(getActivity(), "http://88.198.48.246:9017/app/run", requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("statusCode", statusCode + "");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("statusCode", statusCode + "");
                        Log.i("Response", responseString);
                        RegisterUtils.clearDefaults(getActivity());
                        Toast.makeText(getActivity(), "Phone Number Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), RegisterActivity.class));
                    }
                });
            }
        });
        return view;
    }
}