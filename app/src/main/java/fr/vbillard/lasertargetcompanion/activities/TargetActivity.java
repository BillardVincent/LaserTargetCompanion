package fr.vbillard.lasertargetcompanion.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.Collections;
import java.util.List;

import fr.vbillard.lasertargetcompanion.R;
import fr.vbillard.lasertargetcompanion.dto.TargetDetected;
import fr.vbillard.lasertargetcompanion.processor.ArucoProcessor;
import fr.vbillard.lasertargetcompanion.processor.RedDotProcessor;
import fr.vbillard.lasertargetcompanion.utils.GeometryUtils;

public class TargetActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener {

    private static final String TAG = "QRdetection::Activity";

    private JavaCamera2View mOpenCvCameraView;
    private ArucoProcessor mQRDetector;
    private RedDotProcessor redDotProcessor;

    private Button startResetBtn;
    private Button stopBtn;
    private TextView statusCible;

    TargetDetected result;
    private int frame = 0;
    private int carrence = 0;
    MediaPlayer mp;

    LinearLayout cameraContainer;
    //ListView listTirs;
TextView score;

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
        startResetBtn = findViewById(R.id.startResetBtn);
        stopBtn = findViewById(R.id.stopBtn);
        //listTirs = findViewById(R.id.listTirs);
        score = findViewById(R.id.score);
        statusCible = findViewById(R.id.statusCible);

        cameraContainer = findViewById(R.id.cameraContainer);
        mOpenCvCameraView = new JavaCamera2View(this, -1);
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
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.i(TAG, "Menu Item selected " + item);

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
            result = mQRDetector.handleFrame(src);
        }

        if (result != null){
            // todo cible en mouvement
            statusCible.setText("Cible Acquise");
        } else {
            statusCible.setText("Cible non détectée");
        }

        if (carrence != 0){
            carrence--;
            return src;
        }

        Point shoot = redDotProcessor.handleFrame(src, result);

        if (shoot != null) {
            carrence = 30;
            mp.start();
            int scoreValue = GeometryUtils.getShootValue(shoot);
            TextView tv = new TextView(this);
            tv.setText(String.valueOf(scoreValue));
            //listTirs.addFooterView(tv);
            score.setText(String.valueOf(scoreValue));
            Log.e(TAG, "Cible atteinte " + scoreValue);
        }

        return src;


    }

}