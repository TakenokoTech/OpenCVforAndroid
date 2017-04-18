package tech.takenoko.opencvforandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tech.takenoko.opencvforandroid.utils.OpenCVCamera;
import tech.takenoko.opencvforandroid.view.CameraView;

public class MainActivity extends AppCompatActivity {

    OpenCVCamera openCVCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this, CameraView.class);
        startActivity(intent);
    }

}
