package fr.vbillard.lasertargetcompanion.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;

import fr.vbillard.lasertargetcompanion.R;
import fr.vbillard.lasertargetcompanion.dto.TargetDetected;
import fr.vbillard.lasertargetcompanion.processor.ArucoProcessor;
import fr.vbillard.lasertargetcompanion.processor.RedDotProcessor;
import fr.vbillard.lasertargetcompanion.utils.CustomCameraView;

public class TargetActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener {

    private static final String  TAG = "QRdetection::Activity";

    private CustomCameraView mOpenCvCameraView;
    private ArucoProcessor mQRDetector;
    private RedDotProcessor redDotProcessor;
    private MenuItem mItemQRCodeDetectorAruco;
    private MenuItem             mItemQRCodeDetector;
    private MenuItem             mItemTryDecode;
    private MenuItem             mItemMulti;
    TargetDetected result;
    private int frame = 0;
    MediaPlayer mp;

    LinearLayout cameraContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (OpenCVLoader.initLocal()) {
            Log.i(TAG, "OpenCV loaded successfully");
        } else {
            Log.e(TAG, "OpenCV initialization failed!");
            (Toast.makeText(this, "OpenCV initialization failed!", Toast.LENGTH_LONG)).show();
            return;
        }

        Log.d(TAG, "Creating and setting view");
        mp = MediaPlayer.create(this, R.raw.bip);
        setContentView(R.layout.activity_target);
        cameraContainer = findViewById(R.id.cameraContainer);
        mOpenCvCameraView = new CustomCameraView(this, -1);
        mOpenCvCameraView.setMaxFrameSize(720, 720);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.enableFpsMeter();
        mOpenCvCameraView.setCvCameraViewListener(this);

        mQRDetector = new ArucoProcessor(true);
        redDotProcessor = new RedDotProcessor();

        cameraContainer.addView(mOpenCvCameraView);
        mOpenCvCameraView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.enableView();
        }
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemQRCodeDetectorAruco = menu.add("Aruco-based QR code detector");
        mItemQRCodeDetectorAruco.setCheckable(true);
        mItemQRCodeDetectorAruco.setChecked(true);

        mItemQRCodeDetector = menu.add("Legacy QR code detector");
        mItemQRCodeDetector.setCheckable(true);
        mItemQRCodeDetector.setChecked(false);

        mItemTryDecode = menu.add("Try to decode QR codes");
        mItemTryDecode.setCheckable(true);
        mItemTryDecode.setChecked(true);

        mItemMulti = menu.add("Use multi detect/decode");
        mItemMulti.setCheckable(true);
        mItemMulti.setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.i(TAG, "Menu Item selected " + item);
        if (item == mItemQRCodeDetector && !mItemQRCodeDetector.isChecked()) {
            mQRDetector = new ArucoProcessor(true);
            mItemQRCodeDetector.setChecked(true);
            mItemQRCodeDetectorAruco.setChecked(false);
        } else if (item == mItemQRCodeDetectorAruco && !mItemQRCodeDetectorAruco.isChecked()) {
            mQRDetector = new ArucoProcessor(true);
            mItemQRCodeDetector.setChecked(false);
            mItemQRCodeDetectorAruco.setChecked(true);
        } else if (item == mItemTryDecode) {
            mItemTryDecode.setChecked(!mItemTryDecode.isChecked());
        } else if (item == mItemMulti) {
            mItemMulti.setChecked(!mItemMulti.isChecked());
        }
        return true;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(Mat src) {
        Core.flip(src.t(), src, 1); //mRgba.t() is the transpose
        frame++;
        if (frame >= 30) {
            frame = 0;
            //return inputFrame;
             result= mQRDetector.handleFrame(src);


        }
        else{

        }
        if (redDotProcessor.handleFrame(src, result) != null){
            mp.start();
        }

        return src;


    }

}