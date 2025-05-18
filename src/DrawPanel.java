import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.Random;


import javax.swing.JPanel;

public class DrawPanel extends JPanel implements MouseListener {
    double prevX;
    double prevY;
    double xPos, yPos;
    volatile private boolean mouseDown = false;

    DrawPanel() {
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

        prevX = getXPos(0);
        prevY = getYPos(0);

        long startTime = System.nanoTime();
        for (double t = 0; t <= 1; t += 0.001) {
            xPos = getXPos(t);
            yPos = getYPos(t);


            g2d.setColor(new Color(0,0, 0));
            g2d.setStroke(new BasicStroke(3));
            if (prevX != 0 && prevY != 0) {
                g2d.drawLine((int) prevX, (int) prevY, (int) xPos, (int) yPos);
            }


            prevX = xPos;
            prevY = yPos;
        }
        System.out.println((System.nanoTime() - startTime));

        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        while (tempPoint != null) {
            g2d.setColor(tempPoint.color);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval((int)tempPoint.p.posX - 12, (int)tempPoint.p.posY -12, 25, 25);
            g2d.setColor(Color.gray);
            g2d.setStroke(new BasicStroke(1));
            if(tempPoint.getNextPoint() != null) g2d.drawLine((int)tempPoint.p.posX,(int) tempPoint.p.posY,(int) tempPoint.getNextPoint().p.posX,(int) tempPoint.getNextPoint().p.posY);
            tempPoint = tempPoint.getNextPoint();
        }






    }

    public double getXPos(double t) {
        double x  = 0;
        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        int n = PointList.getFirstPoint().length()-1;
        int k = 0;
        while (tempPoint != null) {
            x += binomalCo(n, k)*Math.pow((1-t), n-k)*Math.pow(t,k)*(tempPoint.p.posX);
            k++;
            tempPoint = tempPoint.getNextPoint();
        }

        return x;
    }

    public double getYPos(double t) {
        double y  = 0;
        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        int n = PointList.getFirstPoint().length()-1;
        int k = 0;
        while (tempPoint != null) {
            y += binomalCo(n, k)*Math.pow((1-t), n-k)*Math.pow(t,k)*(tempPoint.p.posY);
            k++;
            tempPoint = tempPoint.getNextPoint();
        }

        return y;
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

    long binomalCo(int n, int k) {
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