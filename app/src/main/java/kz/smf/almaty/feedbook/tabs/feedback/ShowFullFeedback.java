package kz.smf.almaty.feedbook.tabs.feedback;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kz.smf.almaty.feedbook.R;
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
    Intent intent;

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

        intent = getIntent();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feedback_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_share:
                Bitmap sharedImage = generateImage();
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                if (intent != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setPackage("com.instagram.android");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse
                            (MediaStore.Images.Media.insertImage(getContentResolver(), sharedImage, "I am Happy", "Share happy !")));
                    shareIntent.setType("image/jpeg");

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", desc.getText().toString());
                    clipboard.setPrimaryClip(clip);

                    startActivity(shareIntent);
                } else {
                    // bring user to the market to download the app.
                    // or let them choose an app?
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
                    startActivity(intent);
                }
                break;

            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Bitmap generateImage() {
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.bg_feedback); // the original file yourimage.jpg i added in resources
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);

        logo.buildDrawingCache();
        Bitmap bmap = logo.getDrawingCache();
        bmap = Bitmap.createScaledBitmap(bmap, 250, 250,
                true);

        String title = "Я оставила отзыв:";
        Canvas cs = new Canvas(dest);
        Paint titlePaint = new Paint();
        titlePaint.setTextSize(120);
        titlePaint.setColor(Color.WHITE);
        titlePaint.setStyle(Paint.Style.FILL);
        float heightTitle = titlePaint.measureText("yY");
        float widthTitle = titlePaint.measureText(title);
        float x_coordTitle = (src.getWidth() - widthTitle) / 2;
//        float x_coord_image = (src.getWidth() - bmap.getWidth())/2;
//        float y_coord_image = (src.getHeight() - bmap.getHeight())/2;
//        cs.drawBitmap(bmap, x_coord_image, y_coord_image, tPaint);

        String yourText = desc.getText().toString();
//        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(65);
        tPaint.setColor(Color.WHITE);
        tPaint.setStyle(Paint.Style.FILL);
//        cs.drawBitmap(src, 0f, 0f, null);
        float height = tPaint.measureText("yY");
        float width = tPaint.measureText(yourText);
        float x_coord = (src.getWidth() - width) / 2;


        String v = "в";
        Paint vPaint = new Paint();
        vPaint.setTextSize(120);
        vPaint.setColor(Color.WHITE);
        vPaint.setStyle(Paint.Style.FILL);
//        cs.drawBitmap(src, 0f, 0f, null);
        float vheight = vPaint.measureText("yY");
        float vwidth = vPaint.measureText(v);
        float x_coord_v = (src.getWidth() - vwidth) / 2;

        String placeText = placeName.getText().toString();
        Paint placePaint = new Paint();
        placePaint.setTextSize(100);
        placePaint.setColor(Color.WHITE);
        placePaint.setStyle(Paint.Style.FILL);
//        cs.drawBitmap(src, 0f, 0f, null);
        float place_height = placePaint.measureText("yY");
        float place_width = placePaint.measureText(placeText);
        float x_coord_place = (src.getWidth() - place_width) / 2;

        String addressText = intent.getStringExtra("placeAddress");
        Paint addressPaint = new Paint();
        addressPaint.setTextSize(40);
        addressPaint.setColor(Color.WHITE);
        addressPaint.setStyle(Paint.Style.FILL);
        float address_height = addressPaint.measureText("yY");
        float address_width = addressPaint.measureText(addressText);
        float x_coord_address = (src.getWidth() - address_width) / 2;
//        float x_coord_image = (src.getWidth() - bmap.getWidth())/2;
//        float y_coord_image = (src.getHeight() - bmap.getHeight())/2;
//        cs.drawBitmap(bmap, x_coord_image, y_coord_image, tPaint);


        float x_coord_image = (src.getWidth() - bmap.getWidth()) / 2;
        float y_coord_image = (src.getHeight() - bmap.getHeight()) / 2;

        cs.drawBitmap(src, 0f, 0f, null);
        cs.drawBitmap(bmap, x_coord_image - 400f, y_coord_image + 100f, null);
        cs.drawText(title, x_coordTitle, heightTitle + 400f, titlePaint);
        cs.drawText(yourText, x_coord, height + 600f, tPaint);
        cs.drawText(v, x_coord_v, vheight + 800f, vPaint);
        cs.drawText(placeText, x_coord_place, place_height + 1100f, placePaint);
        cs.drawText(addressText, x_coord_address, address_height + 1400f, addressPaint); // 15f is to put space between top edge and the text, if you want to change it, you can
        //            dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File("/storage/emulated/0/DCIM/Camera/IsImageAfterAddingText.jpg")));
        // dest is Bitmap, if you want to preview the final image, you can display it on screen also before saving
//            imageView.setImageBitmap(dest);
        Log.i("path", Environment.getExternalStorageDirectory().getPath());
        return dest;
    }
}
