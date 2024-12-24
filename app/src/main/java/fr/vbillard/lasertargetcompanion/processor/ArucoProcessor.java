package fr.vbillard.lasertargetcompanion.processor;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2RGB;
import static fr.vbillard.lasertargetcompanion.utils.GeometryUtils.getMiddle;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.ArucoDetector;
import org.opencv.objdetect.Objdetect;

import java.util.ArrayList;
import java.util.List;

import fr.vbillard.lasertargetcompanion.dto.TargetDetected;

public class ArucoProcessor {
    private ArucoDetector detector;
    private static final String TAG = "QRProcessor";
    private Scalar LineColor = new Scalar(255, 0, 0);
    private Scalar FontColor = new Scalar(0, 0, 255);

    public ArucoProcessor(boolean useArucoDetector) {
            detector = new ArucoDetector();

    }

    private boolean findQRs(Mat inputFrame, List<Mat> decodedInfo, Mat points) {
        Mat greyFrame = new Mat();
        Imgproc.cvtColor(inputFrame, greyFrame, COLOR_BGR2GRAY);
        detector.detectMarkers(greyFrame,decodedInfo, points);
        return !decodedInfo.isEmpty();
    }

    // For debug
    private void renderQRs(Mat inputFrame, List<Mat> decodedInfo, MatOfPoint points) {
         if (!decodedInfo.isEmpty()) {
                Objdetect.drawDetectedMarkers(inputFrame, decodedInfo, points);
        }
    }

    /* this method to be called from the outside. It processes the frame to find QR codes. */
    public synchronized TargetDetected handleFrame(Mat inputFrame) {

        List<Mat> decodedInfo = new ArrayList<>();
        MatOfPoint points = new MatOfPoint();
        boolean result = findQRs(inputFrame, decodedInfo, points);

        if (!result) {
            return null;
        }

        TargetDetected targetDetected = null;
            Mat greyFrame = new Mat();
            Imgproc.cvtColor(inputFrame, greyFrame, COLOR_BGR2RGB);
            // renderQRs(greyFrame, decodedInfo, points);

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

        Imgproc.cvtColor(greyFrame, inputFrame, COLOR_BGR2RGB);

        if (points2.size()==4) {
            targetDetected = new TargetDetected();
            targetDetected.addResult(points2);

            Point middle = getMiddle(getMiddle(points2.get(0), points2.get(1)), getMiddle(points2.get(2), points2.get(3)));
                Imgproc.circle( inputFrame,
                        middle,
                        400,
                        new Scalar( 0, 0, 255 ),
                        10,
                        8,
                        0 );
            }


        points.release();
        return targetDetected;
    }

}
