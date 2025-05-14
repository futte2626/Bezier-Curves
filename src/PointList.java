import java.awt.*;

public class PointList {
    private static PointList firstPoint;
    private PointList nextPoint;

    public Point p;
    public Color color;

    public PointList() {
        // special case: Hvis der ikke er et firstPoint
        if(firstPoint == null) {
            firstPoint = this;
            this.nextPoint = null;
        }
        else {
            //looper igennem listen
            PointList tempPoint = firstPoint;
            while (tempPoint != null) {
                //Checker om vi er nået til slutningen
                if (tempPoint.nextPoint == null) {
                    // tilføjer punktet i slutningen
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
        //special case. Hvis vi vil fjerne det første element
        if(firstPoint == this) firstPoint = this.nextPoint;
        // Looper igennem
        PointList tempElement= firstPoint;
        while (tempElement != null) {
            //Hvis vi har fundet elementet
            if(tempElement.nextPoint == this) {
                // Fjerner elementet
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
