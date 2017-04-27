package tech.takenoko.opencvforandroid.utils;

import android.content.Context;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.Getter;
import tech.takenoko.opencvforandroid.R;

/**
 * Created by たけのこ on 2017/04/23.
 */

public class UtilCascade {

    private Context mContext;
    private CascadeClassifier mFaceDetector;
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    @Getter private Mat extractedFaceMat = new Mat();

    UtilCascade(Context context) {
        this.mContext = context;
        File cascadeDir = mContext.getDir("cascade", Context.MODE_PRIVATE);
        File cascadeFile =null;
        try {
            cascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            if (!cascadeFile.exists()) {
                InputStream is = mContext.getResources().openRawResource(R.raw.lbpcascade_frontalface);
                FileOutputStream os = new FileOutputStream(cascadeFile);
                byte[] buffer = new byte[4096];
                int readLen = 0;
                while ((readLen = is.read(buffer)) != -1) os.write(buffer, 0, readLen);
            }
            if (cascadeFile == null) throw new IOException();
            mFaceDetector = new CascadeClassifier(cascadeFile.getAbsolutePath());
            if (mFaceDetector.empty()) throw new IOException();
        } catch (IOException e) {
            mFaceDetector = null;
        }
    }

    Mat execute(Mat rgba) {
        Size mMinFaceSize = new Size(rgba.height() / 5, rgba.height() / 5);
        if (mFaceDetector != null) {
            MatOfRect faces = new MatOfRect();
            mFaceDetector.detectMultiScale(rgba, faces, 1.1, 2, 2, mMinFaceSize, new Size());
            Rect[] facesArray = faces.toArray();
            for (int i = 0; i < facesArray.length; i++) {
                extractedFaceMat = new Mat(rgba, facesArray[i]).clone();;
                Log.i("POINT", "x: " + facesArray[i].tl().x + "  y: " + facesArray[i].tl().y);
                Log.i("POINT", "x: " + facesArray[i].br().x + "  y: " + facesArray[i].br().y);
                Imgproc.rectangle(rgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
            }
        }
        return rgba;
    }

}
