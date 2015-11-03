package kz.smf.feedbook.tabs.scanner_result;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import kz.smf.feedbook.R;
import kz.smf.feedbook.TabsActivity;
import kz.smf.feedbook.tabs.scanner_result.add_comment.AddComment;
import kz.smf.feedbook.tabs.scanner_result.add_comment.place.PlaceAdminstration;
import kz.smf.feedbook.tabs.scanner_result.add_comment.place.PlaceMenu;
import kz.smf.feedbook.tabs.scanner_result.add_comment.place.PlaceSale;

public class ScanCodeResult extends Activity {

    ListView listView;
    MyAdapter adapter;
    String[] arr = {"Оставить отзыв","Акции","Меню","Позвать администратора"};
    int[] arr_image = {R.drawable.comment_add, R.drawable.sale, R.drawable.menu, R.drawable.adminstrator};
    ImageButton btnBack;
    RoundedImageView roundedImage;
    ImageView iconPlace;
    ImageView photoPlace;
    TextView titleTV;
    TextView addressTV;

    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(kz.smf.feedbook.R.layout.scan);

        photoPlace = (ImageView) findViewById(kz.smf.feedbook.R.id.imageViewPhoto);
        iconPlace = (ImageView) findViewById(kz.smf.feedbook.R.id.imageViewIconPlace);
        titleTV = (TextView) findViewById(kz.smf.feedbook.R.id.title);
        addressTV = (TextView) findViewById(kz.smf.feedbook.R.id.address);

        JSONObject jsonCommand = new JSONObject();
        JSONObject jsonParams = new JSONObject();
        RequestParams requestParams = new RequestParams();
        try {
            jsonCommand.put("command", "getPlace");
            jsonCommand.put("params", jsonParams);
            jsonParams.put("code", "2");
            requestParams.add("data", jsonCommand.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        asyncHttpClient.post(ScanCodeResult.this, "http://88.198.48.246:9017/app/run", requestParams, new TextHttpResponseHandler() {
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

                        JSONArray jsonArray = jsonObject.getJSONArray("params");
                        JSONObject jsonParams = jsonArray.getJSONObject(0);

                        JSONObject jsonValue = jsonParams.getJSONObject("value");
                        String title = jsonValue.getString("title");
                        String address = jsonValue.getString("address");

                        JSONObject logo = jsonValue.getJSONObject("logo");
                        String smallLogoUrl = logo.getString("small");

                        JSONObject photo = jsonValue.getJSONObject("photo");
                        String smallPhotoUrl = photo.getString("small");

                        titleTV.setText(title);
                        addressTV.setText(address);
                        Picasso.with(getApplicationContext())
                                .load(smallPhotoUrl)
                                .into(photoPlace);
                        Target target = new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Log.i("Success", "BitmapLoaded");
                                roundedImage = new RoundedImageView(bitmap, 100);
                                iconPlace.setImageDrawable(roundedImage);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        };
                        Picasso.with(getApplicationContext())
                                .load(smallLogoUrl)
                                .into(target);

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        listView = (ListView) findViewById(kz.smf.feedbook.R.id.listViewScanCode);
        adapter = new MyAdapter(this, arr, arr_image);
        listView.setAdapter(adapter);
        btnBack = (ImageButton) findViewById(kz.smf.feedbook.R.id.imageButtonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScanCodeResult.this, TabsActivity.class));
                overridePendingTransition(kz.smf.feedbook.R.anim.left_to_right, kz.smf.feedbook.R.anim.right_to_left);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if (position == 0) {
                    intent = new Intent(ScanCodeResult.this, AddComment.class);
                } else if(position == 1) {
                    intent = new Intent(ScanCodeResult.this, PlaceSale.class);
                } else if(position == 2) {
                    intent = new Intent(ScanCodeResult.this, PlaceMenu.class);
                } else if(position == 3) {
                    intent = new Intent(ScanCodeResult.this, PlaceAdminstration.class);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ScanCodeResult.this, TabsActivity.class));
        overridePendingTransition(kz.smf.feedbook.R.anim.left_to_right, kz.smf.feedbook.R.anim.right_to_left);
    }
    @Override
    public void onDestroy() {  // could be in onPause or onStop
//        Picasso.with(this).cancelRequest(target);
        super.onDestroy();
    }
    private class MyAdapter extends BaseAdapter{
        Activity activity;
        String[] arr;
        int[] arr_image;
        LayoutInflater inflater;
        public MyAdapter(Activity activity, String[] arr, int[] arr_image) {
            this.activity = activity;
            this.arr = arr;
            this.arr_image = arr_image;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public Object getItem(int position) {
            return arr[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder view;
            if (convertView == null) {
                view = new ViewHolder();
                convertView = inflater.inflate(kz.smf.feedbook.R.layout.item_scan_result, null);
                view.title = (TextView) convertView.findViewById(kz.smf.feedbook.R.id.textViewTitle);
                view.imageView = (ImageView) convertView.findViewById(kz.smf.feedbook.R.id.imageViewIcon);
                convertView.setTag(view);
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            view.title.setText(arr[position]);
            view.imageView.setBackgroundResource(arr_image[position]);

            return convertView;
        }

        public class ViewHolder {
            public TextView title;
            public ImageView imageView;
        }
    }
}
