package tech.takenoko.opencvforandroid.opengl;

import android.app.Activity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import tech.takenoko.opencvforandroid.R;

/**
 * Created by takenoko on 2017/02/27.
 */

public class OpenCVCamera implements CameraBridgeViewBase.CvCameraViewListener2 {

    private Activity activity;
    private CameraBridgeViewBase mCameraView;
    private Mat mOutputFrame;

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
//        Imgproc.Canny(inputFrame.rgba(), mOutputFrame, 80, 100);
//        Core.bitwise_not(mOutputFrame, mOutputFrame);
        return rgba;
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
