package fr.vbillard.lasertargetcompanion.dto;

import org.opencv.core.Point;

import java.util.Comparator;
import java.util.List;


public class TargetDetected {

    private Point topLeftPoint;
    private Point topRightPoint;
    private Point bottomLeftPoint;
    private Point bottomRightPoint;


    public Point getTopLeftPoint() {
        return topLeftPoint;
    }

    public void setTopLeftPoint(Point topLeftPoint) {
        this.topLeftPoint = topLeftPoint;
    }

    public Point getTopRightPoint() {
        return topRightPoint;
    }

    public void setTopRightPoint(Point topRightPoint) {
        this.topRightPoint = topRightPoint;
    }

    public Point getBottomLeftPoint() {
        return bottomLeftPoint;
    }

    public void setBottomLeftPoint(Point bottomLeftPoint) {
        this.bottomLeftPoint = bottomLeftPoint;
    }

    public Point getBottomRightPoint() {
        return bottomRightPoint;
    }

    public void setBottomRightPoint(Point bottomRightPoint) {
        this.bottomRightPoint = bottomRightPoint;
    }


    public void addResult(List<Point> points2) {
        points2.sort(Comparator.comparingDouble(c -> c.x));

        if (points2.get(0).y > points2.get(1).y){
            topLeftPoint = points2.get(1);
            topRightPoint = points2.get(0);
        } else{
            topLeftPoint = points2.get(0);
            topRightPoint = points2.get(1);
        }

        if (points2.get(2).y > points2.get(3).y){
            bottomLeftPoint = points2.get(3);
            bottomRightPoint = points2.get(2);
        } else{
            bottomLeftPoint = points2.get(2);
            bottomRightPoint = points2.get(3);
        }
    }
}
