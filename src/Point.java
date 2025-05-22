public class Point {
    public double posX;
    public double posY;

    Point(double x, double y) {
        posX = x;
        posY = y;
    }

    public double distance(Point p) {
        //Returnere afstanden i pixel
        return Math.sqrt(Math.pow(p.posX - posX, 2) + Math.pow(p.posY - posY, 2));
    }
}


