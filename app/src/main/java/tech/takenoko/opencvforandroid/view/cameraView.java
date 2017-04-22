package tech.takenoko.opencvforandroid.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

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
    @BindView(R.id.threshold2_text) TextView threshold2Text;
    @BindView(R.id.toggle_of_canny) ToggleButton enableCannyToggle;
    @BindView(R.id.threshold1_bar) AppCompatSeekBar threshold1SeekBar;
    @BindView(R.id.threshold2_bar) AppCompatSeekBar threshold2SeekBar;
    @BindView(R.id.camera_view) CameraBridgeViewBase mCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        openCVCamera = new OpenCVCamera(this, mCameraView);
        threshold1SeekBar.setOnSeekBarChangeListener(changeSeekBar);
        threshold2SeekBar.setOnSeekBarChangeListener(changeSeekBar);
        enableCannyToggle.setOnCheckedChangeListener(changeToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCVCamera.onResume();
        onUpdate();
    }

    protected void onUpdate() {
        threshold1Text.setText("Threshold1: " + openCVCamera.getModel().getThreshold1());
        threshold2Text.setText("Threshold2: " + openCVCamera.getModel().getThreshold2());
    }

    @OnClick(R.id.toggle_of_canny)
    public void onClickThreshold1Down(Button button) {
        openCVCamera.getModel().sub("threshold1");
    }

    CompoundButton.OnCheckedChangeListener changeToggle = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (compoundButton.getId()) {
                case R.id.toggle_of_canny:
                    openCVCamera.getModel().change("canny", b);
                    break;
            }
            onUpdate();
        }
    };

    SeekBar.OnSeekBarChangeListener changeSeekBar = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            switch (seekBar.getId()) {
                case R.id.threshold1_bar:
                    openCVCamera.getModel().change("threshold1", seekBar.getProgress());
                    break;
                case R.id.threshold2_bar:
                    openCVCamera.getModel().change("threshold2", seekBar.getProgress());
                    break;
            }
            onUpdate();
        }
        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override public void onStopTrackingTouch(SeekBar seekBar) {}
    };
}

