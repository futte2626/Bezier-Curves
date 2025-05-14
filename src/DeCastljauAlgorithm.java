import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class DeCastljauAlgorithm extends JPanel implements MouseListener {
    double prevX;
    double prevY;
    double xPos, yPos;
    volatile private boolean mouseDown = false;

    DeCastljauAlgorithm() {
        this.setPreferredSize(new Dimension(1000, 800));
        repaint();

        addMouseListener(this);
        xPos = 1;
        yPos = 1;


        addPoint(100, 200);
        addPoint(300, 200);
        addPoint(700, 600);
        addPoint(400, 400);

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d =  (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        prevX = 0;
        prevY = 0;


        for (double t = 0; t <= 1; t += 0.001) {

            Point nextPoint = deCasteljau(PointArray(),t);
            xPos = nextPoint.getX();
            yPos = nextPoint.getY();


            g2d.setColor(new Color(0,0, 0));
            g2d.setStroke(new BasicStroke(1));
            if (prevX != 0 && prevY != 0) {
                g2d.drawLine((int) prevX, (int) prevY, (int) xPos, (int) yPos);
            }


            prevX = xPos;
            prevY = yPos;
        }

        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        while (tempPoint != null) {
            g2d.setColor(tempPoint.color);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(tempPoint.p.posX - 12, tempPoint.p.posY -12, 25, 25);
            g2d.setColor(Color.gray);
            g2d.setStroke(new BasicStroke(1));
            if(tempPoint.getNextPoint() != null) g2d.drawLine(tempPoint.p.posX, tempPoint.p.posY, tempPoint.getNextPoint().p.posX, tempPoint.getNextPoint().p.posY);
            tempPoint = tempPoint.getNextPoint();
        }
    }

    private Point[] PointArray() {
        PointList tempPoint = PointList.getFirstPoint();
        int length = tempPoint.length();
        Point[] points = new Point[length];
        for (int i = 0; i < length; i++) {
            points[i] = new Point(tempPoint.p.posX, tempPoint.p.posY);
            tempPoint = tempPoint.getNextPoint();
        }
        return points;
    }

    private Point deCasteljau(Point[] points, double t) {
        if (points.length == 1) {
            return points[0];  // Base case
        }

        Point[] nextLevel = new Point[points.length - 1];
        for (int i = 0; i < nextLevel.length; i++) {
            int x = (int) Math.round((1 - t) * points[i].getX() + t * points[i + 1].getX());
            int y = (int) Math.round((1 - t) * points[i].getY() + t * points[i + 1].getY());
            nextLevel[i] = new Point(x, y);
        }

        return deCasteljau(nextLevel, t); // Recurse
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON3){
            addPoint(e.getX(), e.getY());
        }
        else if(e.getButton() == MouseEvent.BUTTON2){
            Point mosPos = new Point(e.getX(), e.getY());

            PointList tempPoint;
            tempPoint = PointList.getFirstPoint();

            while (tempPoint != null) {
                if(mosPos.distance(tempPoint.p) <= 15) tempPoint.RemovePoint();
                tempPoint = tempPoint.getNextPoint();
            }
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseDown = true;
            Point mosPos = new Point(e.getX(), e.getY());

            PointList tempPoint;
            tempPoint = PointList.getFirstPoint();
            while (tempPoint != null) {
                if(mosPos.distance(tempPoint.p) <= 15) initThread(tempPoint.p);
                tempPoint = tempPoint.getNextPoint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseDown = false;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    long binomalCoefficient(int n, int k) {
        return factorial(n)/(factorial(k)*factorial(n-k));
    }

    long factorial(int n) {
        if(n==0) return 1;
        int res = n;
        for (int i = n-1; i > 0; i--) {
            res *= i;
        }
        return res;
    }

    volatile private boolean isRunning = false;
    private synchronized boolean checkAndMark() {
        if (isRunning) return false;
        isRunning = true;
        return true;
    }
    private void initThread(Point p) {
        if (checkAndMark()) {
            new Thread() {
                public void run() {
                    do {
                        try {
                            p.posX = getMousePosition().x;
                            p.posY = getMousePosition().y;
                        }catch (Exception e) {

                        }

                        repaint();
                    } while (mouseDown);
                    isRunning = false;
                }
            }.start();
        }
    }

    private void addPoint(int x, int y) {
        PointList newPoint;
        Random r= new Random();
        newPoint = new PointList();
        newPoint.p = new Point(x, y);
        newPoint.color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
        repaint();
    }
}
