package tech.takenoko.opencvforandroid.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.takenoko.opencvforandroid.R;

/**
 * Created by たけのこ on 2017/04/23.
 */

public class PreviewView extends Activity {

    @BindView(R.id.preview_view) ImageView imageView;
    @BindView(R.id.preview_submit_button) Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        importIntent();
    }

    private void importIntent() {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String bmpPath = (String) b.get("PREVIEW_IMAGE_PATH");
        if(new File(bmpPath).exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(bmpPath);
            imageView.setImageBitmap(bmp);
        }
    }
}
