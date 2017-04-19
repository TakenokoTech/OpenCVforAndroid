package tech.takenoko.opencvforandroid.utils;

import android.app.Activity;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import lombok.Getter;
import tech.takenoko.opencvforandroid.model.OpenCVModel;

/**
 * Created by takenoko on 2017/02/27.
 */

public class OpenCVCamera implements CameraBridgeViewBase.CvCameraViewListener2 {

    @Getter
    private OpenCVModel model = new OpenCVModel();

    private Activity activity;
    private Mat mOutputFrame;
    private CameraBridgeViewBase mCameraView;

    //=======================================
    // CONSTRACTER
    //=======================================
    public OpenCVCamera(Activity activity, CameraBridgeViewBase mCameraView) {
        this.activity = activity;
        this.mCameraView = mCameraView;
        canUseOpenCV();
        onCreate();
    }

    //=======================================
    // LIFE SYCLE
    //=======================================
    public void onCreate() {
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
        Mat mat = inputFrame.rgba();
        Imgproc.Canny(mat, mOutputFrame, model.getThreshold1(), 100);
//      Core.bitwise_not(mOutputFrame, mOutputFrame);
        return mat;
    }

    private Mat Canny(Mat rgba) {
        Mat mat = rgba;
        Imgproc.Canny(mat, mOutputFrame, model.getThreshold1(), 100);
        return mat;
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

    public boolean canUseOpenCV() {
        if(!OpenCVLoader.initDebug()){
            Log.i("OpenCV", "Failed");
            return false;
        }else{
            Log.i("OpenCV", "successfully built !");
            return true;
        }
    }
}
