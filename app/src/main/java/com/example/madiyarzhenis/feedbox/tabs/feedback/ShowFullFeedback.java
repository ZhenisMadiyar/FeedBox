package com.example.madiyarzhenis.feedbox.tabs.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.madiyarzhenis.feedbox.R;
import com.squareup.picasso.Picasso;

/**
 * Created by madiyarzhenis on 16.10.15.
 */
public class ShowFullFeedback extends Activity {
    TextView title;
    TextView desc;
    TextView created;
    TextView placeName;
    ImageView logo;
    ImageView imageView;
    TextView answer_desc;
    RelativeLayout adminAnswer;
    View viewshka, viewshka2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_full_feedback);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("MyFeedbacks");

        title = (TextView) findViewById(R.id.textViewTitleMyFeedbackFull);
        desc = (TextView) findViewById(R.id.textViewDescMyFeedbackFull);
        created = (TextView) findViewById(R.id.textViewCreatedMyFeedbackFull);
        placeName = (TextView) findViewById(R.id.textViewPlaceName);
        logo = (ImageView) findViewById(R.id.imageViewPlaceLogo);
        imageView = (ImageView) findViewById(R.id.imageViewMyFeedbackFull);
        answer_desc = (TextView) findViewById(R.id.answer_descr);
        adminAnswer = (RelativeLayout) findViewById(R.id.lay_answer);
        viewshka = (View) findViewById(R.id.viewshka);
        viewshka2 = (View) findViewById(R.id.viewshka2);

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("feedTitle"));
        desc.setText(intent.getStringExtra("feedDesc"));
        created.setText(intent.getStringExtra("feedCreated"));
        placeName.setText(intent.getStringExtra("placeName"));

        Picasso.with(this)
                .load(intent.getStringExtra("placeLogo"))
                .into(logo);

        if (intent.getStringExtra("feedPhoto") != null) {
            Picasso.with(this)
                    .load(intent.getStringExtra("feedPhoto"))
                    .into(imageView);
        }

        if (intent.getStringExtra("answerAdmin") != null) {
            adminAnswer.setVisibility(View.VISIBLE);
            viewshka.setVisibility(View.VISIBLE);
            viewshka2.setVisibility(View.VISIBLE);
            answer_desc.setText(intent.getStringExtra("answerAdmin"));
        }

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
