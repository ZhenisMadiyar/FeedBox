package kz.smf.almaty.feedbook.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kz.smf.almaty.feedbook.R;
import kz.smf.almaty.feedbook.RegisterUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

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
                client.post(getActivity(), "http://88.198.48.246:9017/usercontroller/delete?phone=7"+ RegisterUtils.getDefaults(getActivity(),
                        RegisterUtils.TAG_Phone), null, new TextHttpResponseHandler() {
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
        return view;
    }
}