package kz.smf.feedbook.tabs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;
import kz.smf.feedbook.R;
import kz.smf.feedbook.TabsActivity;
import kz.smf.feedbook.tabs.feedback.MyFeedBackAdapter;
import kz.smf.feedbook.tabs.feedback.MyFeedback;
import kz.smf.feedbook.tabs.feedback.ShowFullFeedback;

/**
 * Created by madiyarzhenis on 31.08.15.
 */
public class Comment extends Fragment {

    AsyncHttpClient client = new AsyncHttpClient();
    ListView myFeedBackListView;
    MyFeedBackAdapter adapter;
    ArrayList<MyFeedback> arrayList;
    ArrayList<String> answerAdmin;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_feedback_main, container, false);
        myFeedBackListView = (ListView) view.findViewById(R.id.listViewFeedBack);
        arrayList = new ArrayList<>();
        answerAdmin = new ArrayList<>();

        RequestParams requestParams = new RequestParams();
        JSONObject jsonCommand = new JSONObject();
        try {
            jsonCommand.put("command", "myFeedbacks");
            requestParams.add("data", jsonCommand.toString());
            requestParams.add("userid", String.valueOf(1));
            Log.i("JsonStr", jsonCommand.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        Log.i("DeviceToken", deviceId);
        Log.i("PackageName", getActivity().getPackageName());

        client.get(getActivity(), "http://88.198.48.246:9017/app/run", requestParams, new TextHttpResponseHandler() {
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
                        arrayList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("requests");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonFeedBack = jsonArray.getJSONObject(i);
                            String title = jsonFeedBack.getString("title");
                            String desc = jsonFeedBack.getString("descr");
                            String created = jsonFeedBack.getString("created");
                            if (jsonFeedBack.has("response")) {
                                JSONObject answerObject = jsonFeedBack.getJSONObject("response");
                                answerAdmin.add(answerObject.getString("descr"));
                            }

                            if (jsonFeedBack.has("place") && jsonFeedBack.has("images") && jsonFeedBack.has("response")) {

                                JSONArray jsonArrayImage = jsonFeedBack.getJSONArray("images");
                                JSONObject images = jsonArrayImage.getJSONObject(0);
                                String imageUrl = images.getString("big");

                                JSONObject jsonObjectPlace = jsonFeedBack.getJSONObject("place");
                                JSONObject jsonObjectPlaceLog = jsonObjectPlace.getJSONObject("logo");
                                String logoUrl = jsonObjectPlaceLog.getString("big");
                                String placeName = jsonObjectPlace.getString("title");
                                String placeAddress = jsonObjectPlace.getString("address");

                                JSONObject answerObject = jsonFeedBack.getJSONObject("response");
                                String answerAdmin = answerObject.getString("descr");

                                arrayList.add(new MyFeedback(title, desc, created, imageUrl, logoUrl, placeName, answerAdmin, placeAddress));
                                Log.i("ImageHas", "+");
                            } else if (jsonFeedBack.has("place") && jsonFeedBack.has("images") && !jsonFeedBack.has("response")) {
                                JSONObject jsonObjectPlace = jsonFeedBack.getJSONObject("place");
                                JSONObject jsonObjectPlaceLog = jsonObjectPlace.getJSONObject("logo");
                                String logoUrl = jsonObjectPlaceLog.getString("big");
                                String placeName = jsonObjectPlace.getString("title");
                                String placeAddress = jsonObjectPlace.getString("address");

                                JSONArray jsonArrayImage = jsonFeedBack.getJSONArray("images");
                                JSONObject images = jsonArrayImage.getJSONObject(0);
                                String imageUrl = images.getString("big");
                                arrayList.add(new MyFeedback(title, desc, created, imageUrl, logoUrl, placeName, placeAddress));
                            } else if (jsonFeedBack.has("place") && !jsonFeedBack.has("images") && !jsonFeedBack.has("response")) {
                                JSONObject jsonObjectPlace = jsonFeedBack.getJSONObject("place");
                                JSONObject jsonObjectPlaceLog = jsonObjectPlace.getJSONObject("logo");
                                String logoUrl = jsonObjectPlaceLog.getString("big");
                                String placeName = jsonObjectPlace.getString("title");
                                String placeAddress = jsonObjectPlace.getString("address");
                                arrayList.add(new MyFeedback(title, desc, created, logoUrl, placeName, placeAddress));
                            } else {
                                arrayList.add(new MyFeedback(title, desc, created));
                            }
                        }
                        adapter = new MyFeedBackAdapter(getActivity(), arrayList);
                        myFeedBackListView.setAdapter(adapter);

                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        myFeedBackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                sendNotification("SENDED");

                Intent intent = new Intent(getActivity(), ShowFullFeedback.class);
                intent.putExtra("feedTitle", arrayList.get(position).getTitle());
                intent.putExtra("feedDesc", arrayList.get(position).getDesc());
                intent.putExtra("feedCreated", arrayList.get(position).getCreated());
                intent.putExtra("feedPhoto", arrayList.get(position).getImageUrl());
                intent.putExtra("placeLogo", arrayList.get(position).getLogoUrl());
                intent.putExtra("placeName", arrayList.get(position).getPlaceName());
                intent.putExtra("answerAdmin", arrayList.get(position).getAnswer());
                intent.putExtra("placeAddress", arrayList.get(position).getAddress());
                startActivity(intent);
            }
        });

        return view;
    }
    private void sendNotification(String msg) {
        Intent resultIntent = new Intent(getActivity(), TabsActivity.class);
        resultIntent.putExtra("msg", msg);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getActivity(), 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(getActivity())
                .setContentTitle("Alert")
                .setContentText("You've received new message.You've received new message.You've received new message.You've received new message." +
                        "You've received new message.")
                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText("You've received new message.You've received new message.You've received new message.You've received new message.\" +\n" +
                "                        \"You've received new message.");
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(9001, mNotifyBuilder.build());
    }
}