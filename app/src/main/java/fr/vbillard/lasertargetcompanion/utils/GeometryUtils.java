package fr.vbillard.lasertargetcompanion.utils;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import fr.vbillard.lasertargetcompanion.dto.TargetDetected;

public class GeometryUtils {

    private final static double transposedLength = 800;

    /** valeur en mm*/
    private final static double realLength = 190;



    /** valeur en mm*/
    private final static double rayonMouche = 2.5;
    /** valeur en mm*/
    private final static double rayon10 = 5.75;
    /** valeur en mm*/
    private final static double rayonSupplementaire = 8;


    public static Point getMiddle(Point p1, Point p2) {
        return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }

    public static Mat transposeMat(Mat m, TargetDetected target) {
        MatOfPoint2f src = new MatOfPoint2f(target.getTopLeftPoint(), target.getTopRightPoint(),
                target.getBottomLeftPoint(), target.getBottomRightPoint());
        MatOfPoint2f dst = new MatOfPoint2f(
                new Point(0, 0),
                new Point(transposedLength, 0),
                new Point(0, transposedLength ),
                new Point(transposedLength, transposedLength)
        );
        Mat warpMat = Imgproc.getPerspectiveTransform(src, dst);
        Mat destImage = new Mat();
        Imgproc.warpPerspective(m, destImage, warpMat, m.size());
        return destImage;
    }

    static double ratioPixelCm(){
        return transposedLength/realLength;
    }

    public static int getShootValue(Point p){

        double distanceFromMiddle = Math.sqrt(carreDeLaDistanceDepuisLeMilieu(p.x) + carreDeLaDistanceDepuisLeMilieu(p.y));

        if (rayonMouche > distanceFromMiddle){
            return 11;
        }
        int score = 10;

        double rayonTeste = rayon10;

        while (score > 0){
            if (rayonTeste*ratioPixelCm()>distanceFromMiddle){
                return score;
            }
            score--;
            rayonTeste+= rayonSupplementaire;
        }

        return 0;
    }

    static double carreDeLaDistanceDepuisLeMilieu(double x1){
        double distance = Math.abs(x1-transposedLength/2);
        return distance*distance;
    }
}
