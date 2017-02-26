package tech.takenoko.opencvforandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

import tech.takenoko.opencvforandroid.opengl.OpenCVCamera;

public class MainActivity extends AppCompatActivity {

    OpenCVCamera openCVCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if(!OpenCVLoader.initDebug()){
            Log.i("OpenCV", "Failed");
        }else{
            Log.i("OpenCV", "successfully built !");
        }

        openCVCamera = new OpenCVCamera(this);
        openCVCamera.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCVCamera.onResume();
    }
}
