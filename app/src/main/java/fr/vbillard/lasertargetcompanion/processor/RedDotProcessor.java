package fr.vbillard.lasertargetcompanion.processor;

import static org.opencv.core.CvType.CV_8U;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import fr.vbillard.lasertargetcompanion.dto.TargetDetected;

public class RedDotProcessor {
    private static final String TAG = "RedDotProcessor";

    private static int i = 0;
    public static final int CROP = 0;
    private Mat mat1;
    private Mat mat2;

    private final static Scalar WHITE = new Scalar(255, 255, 255);

    /**
     * Valeurs hautes et basses de détection en bleu (opencv est en BGR)
     */
    private final static Scalar RANGE_HIGHT = new Scalar(130, 255, 255);
    private final static Scalar RANGE_LOW = new Scalar(110, 100, 100);


    public RedDotProcessor() {

    }

    /**
     * Dans la zone entre les marqueurs, on détect un point rouge
     *
     * @param src
     * @param result
     * @return
     */
    public Point handleFrame(Mat src, TargetDetected result) {

        if (result == null) {
            return null;
        }
        mat1 = new Mat(src.size(), CvType.CV_16UC4);
        mat2 = new Mat(src.size(), CvType.CV_16UC4);

        Imgproc.cvtColor(src, mat1, Imgproc.COLOR_BGR2HSV);

        Mat mask = Mat.zeros(src.size(), CV_8U);

        Point topLeft = new Point(result.getTopLeftPoint().x - CROP, result.getTopLeftPoint().y + CROP);
        Point topRight = new Point(result.getTopRightPoint().x - CROP, result.getTopRightPoint().y - CROP);
        Point bottomLeft = new Point(result.getBottomLeftPoint().x + CROP, result.getTopLeftPoint().y + CROP);
        Point bottomRight = new Point(result.getBottomRightPoint().x - CROP, result.getBottomRightPoint().y - CROP);

        MatOfPoint points = new MatOfPoint(topLeft, topRight, bottomRight, bottomLeft, topLeft);
        Imgproc.fillConvexPoly(mask, points, WHITE);

        Core.inRange(mat1, RANGE_LOW, RANGE_HIGHT, mat2);
        Core.MinMaxLocResult mmG = Core.minMaxLoc(mat2, mask);

        if (!mmG.maxLoc.equals(topLeft)) {
            Imgproc.circle(src, mmG.maxLoc, 30, new Scalar(0, 255, 0), 5, Imgproc.LINE_AA);
            return mmG.maxLoc;
        }

         /*
        mat1 = new Mat(src.size(), CvType.CV_16UC4);
        mat2 = new Mat(src.size(), CvType.CV_16UC4);

        Imgproc.cvtColor(src, mat1, Imgproc.COLOR_BGR2HSV);
        Core.inRange(mat1, RANGE_LOW, RANGE_HIGHT, mat2);
        Core.MinMaxLocResult mmG = Core.minMaxLoc(mat2);
        if (!mmG.maxLoc.equals(new Point(0,0))) {
            Imgproc.circle(src, mmG.maxLoc, 30, new Scalar(0, 255, 0), 5, Imgproc.LINE_AA);
            Log.e(TAG, "Tir enregistré" + ++i);
        }

          */

        return null;

    }
}
