package tech.takenoko.opencvforandroid.utils;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import tech.takenoko.opencvforandroid.R;

/**
 * Created by takenoko on 2017/02/27.
 */

public class OpenCVCamera implements CameraBridgeViewBase.CvCameraViewListener2 {

    private Activity activity;
    private CameraBridgeViewBase mCameraView;
    private Mat mOutputFrame;
    private int threshold1 = 80;

    // VIEW
    private TextView threshold1Text;
    private Button threshold1UpButton;
    private Button threshold1DownButton;

    //=======================================
    // CONSTRACTER
    //=======================================
    public OpenCVCamera(Activity activity) {
        this.activity = activity;
    }

    //=======================================
    // LIFE SYCLE
    //=======================================
    public void onCreate() {
        mCameraView = (CameraBridgeViewBase) activity.findViewById(R.id.camera_view);
        mCameraView.setCvCameraViewListener(this);

        threshold1Text = (TextView)activity.findViewById(R.id.threshold1_text);
        threshold1Text.setText(threshold1 + "");
        threshold1UpButton = (Button)activity.findViewById(R.id.threshold1_up);
        threshold1UpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threshold1++;
                threshold1Text.setText(threshold1 + "");
            }
        });
        threshold1DownButton = (Button)activity.findViewById(R.id.threshold1_down);
        threshold1DownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threshold1--;
                threshold1Text.setText(threshold1 + "");
            }
        });
    }

    public void onResume() {
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, activity, mLoaderCallback);
    }

    //=======================================
    // PUBLIC
    //=======================================
    @Override
    public void onCameraViewStarted(int width, int height) {
        mOutputFrame = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mOutputFrame.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        Imgproc.Canny(inputFrame.rgba(), mOutputFrame, threshold1, 100);
//        Core.bitwise_not(mOutputFrame, mOutputFrame);
        return mOutputFrame;
    }

    //=======================================
    // PRIVATE
    //=======================================
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(activity) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    mCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };
}
