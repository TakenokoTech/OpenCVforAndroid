package tech.takenoko.opencvforandroid.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;

import tech.takenoko.opencvforandroid.R;
import tech.takenoko.opencvforandroid.utils.OpenCVCamera;

/**
 * Created by takenoko on 2017/04/18.
 */

public class CameraView extends Activity{

    OpenCVCamera openCVCamera;

    // VIEW
    private TextView threshold1Text;
    private Button threshold1UpButton;
    private Button threshold1DownButton;

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

