package fr.vbillard.lasertargetcompanion.utils;

import org.opencv.core.Point;

public class GeometryUtils {

    public static Point getMiddle(Point p1, Point p2){
        return new Point ((p1.x+ p2.x)/2, (p1.y+ p2.y)/2);
    }
}
