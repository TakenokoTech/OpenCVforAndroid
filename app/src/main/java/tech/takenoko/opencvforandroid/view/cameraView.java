package tech.takenoko.opencvforandroid.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.CameraBridgeViewBase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.takenoko.opencvforandroid.R;
import tech.takenoko.opencvforandroid.utils.OpenCVCamera;

/**
 * Created by takenoko on 2017/04/18.
 */

public class CameraView extends Activity {

    private OpenCVCamera openCVCamera;

    // VIEW
    @BindView(R.id.threshold1_text) TextView threshold1Text;
    @BindView(R.id.threshold1_up) Button threshold1UpButton;
    @BindView(R.id.threshold1_down) Button threshold1DownButton;
    @BindView(R.id.camera_view) CameraBridgeViewBase mCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        openCVCamera = new OpenCVCamera(this, mCameraView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCVCamera.onResume();
    }

    @OnClick(R.id.threshold1_up)
    public void onClickThreshold1Up(Button button) {
        openCVCamera.getModel().add("threshold1");
        threshold1Text.setText(openCVCamera.getModel().getThreshold1() + "");
    }

    @OnClick(R.id.threshold1_down)
    public void onClickThreshold1Down(Button button) {
        openCVCamera.getModel().sub("threshold1");
        threshold1Text.setText(openCVCamera.getModel().getThreshold1() + "");
    }

}

