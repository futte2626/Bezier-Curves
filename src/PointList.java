public class PointList {
    private static PointList firstPoint;
    private PointList nextPoint;

    public Point p;

    public PointList() {
        if(firstPoint == null) {
            firstPoint = this;
            this.nextPoint = null;
        }
        else {
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

    public int pointNumber() {
        int pointNumber = 0;
        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        while (tempPoint != null) {
            if (tempPoint.nextPoint == this) {
                break;
            }
            pointNumber++;
            tempPoint = tempPoint.getNextPoint();
        }

        return pointNumber;
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
