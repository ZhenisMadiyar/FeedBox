package kz.smf.almaty.feedbook.tabs.scanner_result.add_comment.place;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import kz.smf.almaty.feedbook.R;

/**
 * Created by madiyarzhenis on 28.10.15.
 */
public class PlaceMenu extends Activity {

    ListView listViewMenu;
    String[] menu = {"Menu1", "Menu2", "Menu3", "Menu4", "Menu5", "Menu6", "Menu7"};
    MenuCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_menu);

        getActionBar().setTitle("Меню");
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        listViewMenu = (ListView) findViewById(R.id.listViewMenu);
        adapter = new MenuCustomAdapter(this, menu);
        listViewMenu.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private class MenuCustomAdapter extends BaseAdapter {
        Activity activity;
        String[] menu;
        LayoutInflater inflater;
        public MenuCustomAdapter(Activity activity, String[] menu) {
            this.activity = activity;
            this.menu = menu;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return menu.length;
        }

        @Override
        public Object getItem(int position) {
            return menu[position];
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
                convertView = inflater.inflate(R.layout.item_place_menu, null);
                view.saleTV = (TextView) convertView.findViewById(R.id.textViewMenu);
                convertView.setTag(view);
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            view.saleTV.setText(menu[position]);
            return convertView;
        }

        public class ViewHolder {
            public TextView saleTV;
        }
    }

}
