package fr.vbillard.lasertargetcompanion.utils;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.util.AttributeSet;

import org.opencv.android.FpsMeter;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;

public class CustomCameraView extends JavaCamera2View {

        public CustomCameraView(Context context, int attrs) {
            super(context, attrs);
        }

        public FpsMeter getFpsMeter(){
            return mFpsMeter;
        }

}
