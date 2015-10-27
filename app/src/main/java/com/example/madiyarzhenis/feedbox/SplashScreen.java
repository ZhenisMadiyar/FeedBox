package com.example.madiyarzhenis.feedbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.madiyarzhenis.feedbox.register.RegisterActivity;

/**
 * Created by madiyarzhenis on 15.10.15.
 */
public class SplashScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        if (RegisterUtils.getDefaults(this, RegisterUtils.TAG_Name) == null) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else {
            Log.i("prefName", RegisterUtils.getDefaults(this, RegisterUtils.TAG_Name));
            startActivity(new Intent(this, TabsActivity.class));
        }
    }
}
