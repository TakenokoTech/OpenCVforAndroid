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

import static org.opencv.core.Core.getNumThreads;
import static org.opencv.core.Core.getNumberOfCPUs;

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
        Log.i("getNumThreads: ", String.valueOf(getNumThreads()));
        Log.i("getNumberOfCPUs: ", String.valueOf(getNumberOfCPUs()));
    }

    @Override
    public void onCameraViewStopped() {
        mOutputFrame.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat mat = inputFrame.rgba();
//      Core.bitwise_not(mOutputFrame, mOutputFrame);
        if(model.isEnableCanny()) mat = Canny(mat);
        return mat;
    }

    private Mat Canny(Mat rgba) {
        Imgproc.Canny(rgba, mOutputFrame, model.getThreshold1(), model.getThreshold2());
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
