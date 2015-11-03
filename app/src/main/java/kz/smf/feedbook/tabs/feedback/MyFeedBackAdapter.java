package kz.smf.feedbook.tabs.feedback;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by madiyarzhenis on 16.10.15.
 */
public class MyFeedBackAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<MyFeedback> myFeedbacks;
    LayoutInflater inflater;
    public MyFeedBackAdapter(Activity activity, ArrayList<MyFeedback> arrayList) {
        this.activity = activity;
        this.myFeedbacks = arrayList;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return myFeedbacks.size();
    }

    @Override
    public Object getItem(int position) {
        return myFeedbacks.get(position);
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
            convertView = inflater.inflate(kz.smf.feedbook.R.layout.my_feedback_item, null);
            view.title = (TextView) convertView.findViewById(kz.smf.feedbook.R.id.textViewTitleMyFeedback);
            view.desc = (TextView) convertView.findViewById(kz.smf.feedbook.R.id.textViewDescMyFeedback);
            view.created = (TextView) convertView.findViewById(kz.smf.feedbook.R.id.textViewCreatedMyFeedback);
            view.imageView = (ImageView) convertView.findViewById(kz.smf.feedbook.R.id.imageViewMyFeedback);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        view.title.setText(myFeedbacks.get(position).getTitle());
        view.desc.setText(myFeedbacks.get(position).getDesc());
        view.created.setText(myFeedbacks.get(position).getCreated());
        if (myFeedbacks.get(position).getImageUrl() != null) {
            Log.i("ImageHasAdapter", "+");
            Picasso.with(activity)
                    .load(myFeedbacks.get(position).getLogoUrl())
                    .into(view.imageView);
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView title;
        public TextView desc;
        public TextView created;
        public ImageView imageView;
    }
}
