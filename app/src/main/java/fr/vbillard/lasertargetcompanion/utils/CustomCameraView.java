package fr.vbillard.lasertargetcompanion.utils;

import android.content.Context;

import org.opencv.android.FpsMeter;
import org.opencv.android.JavaCamera2View;

public class CustomCameraView extends JavaCamera2View {

        public CustomCameraView(Context context, int attrs) {
            super(context, attrs);
        }

        public FpsMeter getFpsMeter(){
            return mFpsMeter;
        }

}
