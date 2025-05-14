import java.awt.*;

public class PointList {
    private static PointList firstPoint;
    private PointList nextPoint;

    public Point p;
    public Color color;

    public PointList() {
        if(firstPoint == null) {
            firstPoint = this;
            this.nextPoint = null;
        }
        else {
            //looper igennem listen
            PointList tempPoint = firstPoint;
            while (tempPoint != null) {
                if (tempPoint.nextPoint == null) {
                    this.nextPoint = null;
                    tempPoint.nextPoint = this;
                    break;
                }
                tempPoint = tempPoint.nextPoint;
            }
        }
    }

    public static PointList getFirstPoint() {
        return firstPoint;
    }

    public PointList getNextPoint() {
        return nextPoint;
    }

    public void RemovePoint() {
        //handle special case (if we want to remove firstElement)
        if(firstPoint == this) firstPoint = this.nextPoint;
        //handle general case
        PointList tempElement= firstPoint;
        while (tempElement != null) {
            if(tempElement.nextPoint == this) {
                tempElement.nextPoint = this.nextPoint;
            }
            tempElement = tempElement.nextPoint;
        }
    }

    public int length() {
        int length = 0;
        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        while (tempPoint != null) {
            length++;
            tempPoint = tempPoint.getNextPoint();
        }
        return length;
    }

}
