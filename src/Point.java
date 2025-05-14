public class Point {
    public int posX;
    public int posY;

    Point(int x, int y) {
        posX = x;
        posY = y;
    }

    int getX() {
        return posX;
    }

    int getY() {
        return posY;
    }

    public double distance(Point p) {
        //Bruger pythagoras til at finde afstand og returnere afstanden i pixel
        return Math.sqrt(Math.pow(p.getX() - posX, 2) + Math.pow(p.getY() - posY, 2));
    }
}


