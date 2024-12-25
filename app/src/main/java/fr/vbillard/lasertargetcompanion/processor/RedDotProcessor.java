package fr.vbillard.lasertargetcompanion.processor;

import static org.opencv.core.CvType.CV_8U;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.ArrayList;
import java.util.List;

import fr.vbillard.lasertargetcompanion.dto.TargetDetected;
import fr.vbillard.lasertargetcompanion.utils.GeometryUtils;

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
    private final static Scalar RANGE_LOW = new Scalar(110, 150, 150);


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
        MatOfPoint points = new MatOfPoint(result.getTopLeftPoint(), result.getTopRightPoint(),
                result.getBottomLeftPoint(), result.getBottomRightPoint(), result.getTopLeftPoint());
        Imgproc.fillConvexPoly(mask, points, WHITE);

        Core.inRange(mat1, RANGE_LOW, RANGE_HIGHT, mat2);
        Core.MinMaxLocResult mmG = Core.minMaxLoc(mat2, mask);

        if (!mmG.maxLoc.equals(result.getTopLeftPoint()) && !mmG.maxLoc.equals(result.getBottomLeftPoint())) {
            Imgproc.circle(src, mmG.maxLoc, 30, new Scalar(0, 255, 0), 5, Imgproc.LINE_AA);

            Mat matTransposed = GeometryUtils.transposeMat(mat2, result);
            return Core.minMaxLoc(matTransposed).maxLoc;

        }

        return null;

    }

    /*
    Point  contours(Mat src) {
        final Mat dst = new Mat(src.rows(), src.cols(), src.type());
        src.copyTo(dst);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_BGR2GRAY);
        final List<MatOfPoint> points = new ArrayList<>();
        final Mat hierarchy = new Mat();
        Imgproc.findContours(dst, points, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        Moments moments = Imgproc.moments(points[0]);

        Point centroid = new Point();

        centroid.x = moments.get_m10() / moments.get_m00();
        centroid.y = moments.get_m01() / moments.get_m00();
    }

     */


}
