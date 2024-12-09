package fr.vbillard.lasertargetcompanion;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2RGB;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.ArucoDetector;
import org.opencv.objdetect.Objdetect;

import java.util.ArrayList;
import java.util.List;

public class QRProcessor {
    private ArucoDetector detector;
    private static final String TAG = "QRProcessor";
    private Scalar LineColor = new Scalar(255, 0, 0);
    private Scalar FontColor = new Scalar(0, 0, 255);

    public QRProcessor(boolean useArucoDetector) {
            detector = new ArucoDetector();

    }

    private boolean findQRs(Mat inputFrame, List<Mat> decodedInfo, Mat points,
                           boolean tryDecode, boolean multiDetect) {

        Mat greyFrame = new Mat();
        Imgproc.cvtColor(inputFrame, greyFrame, COLOR_BGR2GRAY);

        detector.detectMarkers(greyFrame,decodedInfo, points);
        return !decodedInfo.isEmpty();

        /*
        if (multiDetect) {
            if (tryDecode)
                result = detector.detectAndDecodeMulti(inputFrame, decodedInfo, points);
            else
                result = detector.detectMulti(inputFrame, points);
        }
        else {
            if(tryDecode) {
                String s = detector.detectAndDecode(inputFrame, points);
                result = !points.empty();
                if (result)
                    decodedInfo.add(s);
            }
            else {
                result = detector.detect(inputFrame, points);
            }
        }
        return result;
         */

    }

    private void renderQRs(Mat inputFrame, List<Mat> decodedInfo, MatOfPoint points) {

        Log.i(TAG, "channels " + inputFrame.channels());
            if (!decodedInfo.isEmpty()) {
                Objdetect.drawDetectedMarkers(inputFrame, decodedInfo, points);
        }
    }

    /* this method to be called from the outside. It processes the frame to find QR codes. */
    public synchronized Mat handleFrame(Mat inputFrame, boolean tryDecode, boolean multiDetect) {
        Core.flip(inputFrame.t(), inputFrame, 1); //mRgba.t() is the transpose

        List<Mat> decodedInfo = new ArrayList<Mat>();
        MatOfPoint points = new MatOfPoint();
        boolean result = findQRs(inputFrame, decodedInfo, points, tryDecode, multiDetect);

        Mat greyFrame = new Mat();
        Imgproc.cvtColor(inputFrame, greyFrame, COLOR_BGR2RGB);

        if (result) {
            renderQRs(greyFrame, decodedInfo, points);

            List<Point> points2= new ArrayList<>();

            for (int i = 0; i < decodedInfo.size(); i++) {
                Mat coordinates = decodedInfo.get(i); //get item in List
                List<Point> corners_xy = new ArrayList<>(4);
                for (int r = 0; r < coordinates.rows(); r++) {
                    for (int c = 0; c < coordinates.cols(); c++) {
                        corners_xy.add(new Point((int)coordinates.get(r,c)[0], (int)coordinates.get(r,c)[1])); // x,y point
                    }
                }
                points2.add(corners_xy.get(0));
            }

            if (points2.size()>3) {
                Point middle = getMiddle(getMiddle(points2.get(0), points2.get(1)), getMiddle(points2.get(2), points2.get(3)));
                Imgproc.circle( greyFrame,
                        middle,
                        400,
                        new Scalar( 0, 0, 255 ),
                        10,
                        8,
                        0 );
            }

        }
        points.release();
        return greyFrame;
    }

    Point getMiddle(Point p1, Point p2){
        return new Point ((p1.x+ p2.x)/2, (p1.y+ p2.y)/2);
    }
}
