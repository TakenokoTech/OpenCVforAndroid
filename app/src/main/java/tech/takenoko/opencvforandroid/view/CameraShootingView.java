package tech.takenoko.opencvforandroid.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.opencv.android.CameraBridgeViewBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.takenoko.opencvforandroid.R;
import tech.takenoko.opencvforandroid.utils.OpenCVCamera;

import static org.opencv.android.CameraBridgeViewBase.CAMERA_ID_BACK;
import static org.opencv.android.CameraBridgeViewBase.CAMERA_ID_FRONT;

/**
 * Created by takenoko on 2017/04/18.
 */

public class CameraShootingView extends Activity {

    private OpenCVCamera openCVCamera;

    // VIEW
    @BindView(R.id.threshold1_text) TextView threshold1Text;
    @BindView(R.id.threshold2_text) TextView threshold2Text;
    @BindView(R.id.toggle_of_camera_id) ToggleButton isCameraIdIsFront;
    @BindView(R.id.toggle_of_canny) ToggleButton enableCannyToggle;
    @BindView(R.id.threshold1_bar) AppCompatSeekBar threshold1SeekBar;
    @BindView(R.id.threshold2_bar) AppCompatSeekBar threshold2SeekBar;
    @BindView(R.id.camera_view) CameraBridgeViewBase mCameraView;
    @BindView(R.id.shooting_image_button) Button shootingImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        openCVCamera = new OpenCVCamera(this, mCameraView);
        threshold1SeekBar.setOnSeekBarChangeListener(changeSeekBar);
        threshold2SeekBar.setOnSeekBarChangeListener(changeSeekBar);
        isCameraIdIsFront.setOnCheckedChangeListener(changeToggle);
        enableCannyToggle.setOnCheckedChangeListener(changeToggle);
        mCameraView.setCameraIndex(CAMERA_ID_FRONT);
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

    @OnClick(R.id.shooting_image_button)
    public void onClickShootingImage(Button button) {
        try {
            Bitmap bitmap = openCVCamera.convertToImage();
            File imageFile = new File(getCacheDir().getAbsolutePath(), "cache.png");
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
            Intent intent = new Intent(this, PreviewView.class);
            intent.putExtra("PREVIEW_IMAGE_PATH", imageFile.getAbsolutePath());
            startActivity(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    CompoundButton.OnCheckedChangeListener changeToggle = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (compoundButton.getId()) {
                case R.id.toggle_of_camera_id:
                    /** TODO: Unimplemented */
                    int id = b ? CAMERA_ID_FRONT : CAMERA_ID_BACK;
                    mCameraView.setCameraIndex(id);
                    break;
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

