package com.example.madiyarzhenis.feedbox.tabs.scanner_result.add_comment.place;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.madiyarzhenis.feedbox.R;

/**
 * Created by madiyarzhenis on 28.10.15.
 */
public class PlaceAdminstration extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_admin);

        getActionBar().setTitle("Админстратор");
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
