package kz.smf.feedbook.tabs.scanner_result.add_comment.place;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by madiyarzhenis on 28.10.15.
 */
public class PlaceSale extends Activity {

    ListView listViewSale;
    SaleCustomAdapter adapter;
    String[] sale = {"-50%", "-10%", "-90%", "-5%", "-20%", "-70%"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(kz.smf.feedbook.R.layout.place_sale);

        getActionBar().setTitle("Скидка");
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        listViewSale = (ListView) findViewById(kz.smf.feedbook.R.id.listViewSale);
        adapter = new SaleCustomAdapter(this, sale);
        listViewSale.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private class SaleCustomAdapter extends BaseAdapter {
        Activity activity;
        String[] sale;
        LayoutInflater inflater;
        public SaleCustomAdapter(Activity activity, String[] sale) {
            this.activity = activity;
            this.sale = sale;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return sale.length;
        }

        @Override
        public Object getItem(int position) {
            return sale[position];
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
                convertView = inflater.inflate(kz.smf.feedbook.R.layout.item_place_sale, null);
                view.saleTV = (TextView) convertView.findViewById(kz.smf.feedbook.R.id.textViewSale);
                convertView.setTag(view);
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            view.saleTV.setText(sale[position]);
            return convertView;
        }

        public class ViewHolder {
            public TextView saleTV;
        }
    }
}
