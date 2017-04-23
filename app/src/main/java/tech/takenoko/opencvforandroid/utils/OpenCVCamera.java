package tech.takenoko.opencvforandroid.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import lombok.Getter;
import tech.takenoko.opencvforandroid.model.OpenCVModel;

import static org.opencv.core.Core.NORM_MINMAX;
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
    private UtilCascade utilCascade;

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
        utilCascade = new UtilCascade(activity);
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
//        mat = markerKeypoint(mat);
        mat = foundFace(mat);
        return mat;
    }

    public Bitmap convertToImage() {
//        Mat src = mOutputFrame.clone();
        Mat src = utilCascade.getExtractedFaceMat();
        Mat dst = new Mat();
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2RGBA, 4);
        Bitmap img = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, img);
        return img;
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

    private Mat Canny(Mat rgba) {
        Imgproc.Canny(rgba, mOutputFrame, model.getThreshold1(), model.getThreshold2());
        return mOutputFrame;
    }

    private Mat markerKeypoint(Mat rgba) {
        Mat gray = null, mat = null;
        //Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_BGRA2GRAY);
        Core.normalize(rgba, rgba, 0, 255, NORM_MINMAX);

        MatOfKeyPoint keyPoint = new MatOfKeyPoint();
        FeatureDetector siftDetector = FeatureDetector.create(FeatureDetector.ORB);
        siftDetector.detect(rgba, keyPoint);

        Mat matchedImage = new Mat(rgba.rows(), rgba.cols(), rgba.type());
        mOutputFrame = matchedImage;
        return matchedImage;
    }

    private Mat foundFace(Mat rgba) {
        return mOutputFrame = utilCascade.execute(rgba);
    }
}
