import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DrawPanel extends JPanel implements MouseListener, ItemListener, ChangeListener {
    double prevX;
    double prevY;
    double xPos, yPos, xSpeed, ySpeed,xAcc,yAcc;
    double arcLength = 0;
    volatile private boolean mouseDown = false;
    private JToggleButton toggleButton;
    Color[] colors = {
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.ORANGE,
            Color.MAGENTA,
            Color.CYAN,
            Color.PINK,
            Color.YELLOW,
    };
    private int pointN = 3;
    private JSlider slider;
    private JLabel label;

    DrawPanel() {

        this.setPreferredSize(new Dimension(1000, 800));
        repaint();

        this.setJToggleButton();
        this.SetAction();
        addMouseListener(this);

        /* Fire cløver
        addPoint(400, 400);
        addPoint(600, 400);
        addPoint(700, 300);
        addPoint(700, 200);
        addPoint(600, 300);
        addPoint(500, 300);
        addPoint(500, 200);
        addPoint(600, 100);
        addPoint(500, 100);
        addPoint(400, 200);
        addPoint(400, 400);
        */

        /*
        addPoint(267, 801);
        addPoint(320, 267);
        addPoint(374, 267);
        addPoint(427, 134);

        addPoint(427, 134);
        addPoint(481, 801);
        addPoint(534, 801);
        addPoint(587, 134);

        addPoint(587, 134);
        addPoint(641, 267);
        addPoint(694, 267);
        addPoint(748, 801); */

        addPoint(100,100);
        addPoint(500, 300);
        addPoint(400,100);

        slider = new JSlider(0, 10000, 10000);
        slider.setPreferredSize(new Dimension(300, 30));
        slider.addChangeListener(this);
        label = new JLabel("t = " + ((double)slider.getValue())/10000);

        this.add(slider);
        this.add(label);

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d =  (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        prevX = getXPos(0);
        prevY = getYPos(0);
        arcLength = 0;

        for (double t = 0; t <= ((double)slider.getValue())/10000; t += 0.0001) {
            if(toggleButton.isSelected()) {
                Point p = deCasteljau(PointArray(), t, null, 0);
                xPos = p.posX;
                yPos = p.posY;
            }
            else {
                xPos = getXPos(t);
                xSpeed = getXSpeed(t);
                xAcc = getXAcc(t);
                yPos = getYPos(t);
                ySpeed = getYSpeed(t);
                yAcc = getYAcc(t);
            }


            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3));
            if (prevX != 0 && prevY != 0) {
                g2d.drawLine((int) prevX, (int) prevY, (int) xPos, (int) yPos);

            }
//            g2d.fillOval((int) xPos, (int)(100*(yPos-prevY)/(xPos-prevX)+ getHeight()/2), 10, 10);
            arcLength += Math.sqrt(Math.pow(xPos - prevX, 2) + Math.pow(yPos - prevY, 2));

            prevX = xPos;

            prevY = yPos;


        }

        g2d.setColor(Color.red);
        g2d.setStroke(new BasicStroke(2));
        Point spdPoint = new Point((xPos+(xSpeed)/10), (yPos+ySpeed/10));
        g2d.drawLine((int) xPos, (int) yPos, (int) (xPos+(xSpeed)/10), (int) (yPos+ySpeed/10));
        g2d.setColor(Color.green);
        g2d.drawLine((int) spdPoint.posX, (int) spdPoint.posY, (int) (spdPoint.posX+xAcc/10), (int) (spdPoint.posY+yAcc/10));

        System.out.println(arcLength);

        if (toggleButton.isSelected()) {
            deCasteljau(PointArray(), ((double)slider.getValue())/10000, g2d, 0);
            g2d.setColor(Color.black);
            g2d.fillOval((int) xPos-5, (int) yPos-5, 10, 10);
        }

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
 //         g2d.drawLine(0,getHeight()/2, getWidth(), getHeight()/2);
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

    public Point deCasteljau(Point[] points, double t, Graphics2D g, int depth) {
        if (points.length == 1) {
            return points[0];
        }

        Point[] nextLevel = new Point[points.length - 1];
        for (int i = 0; i < nextLevel.length; i++) {
            double x = (1 - t) * points[i].posX + t * points[i + 1].posX;
            double y = (1 - t) * points[i].posY + t * points[i + 1].posY;
            nextLevel[i] = new Point(x, y);
            if(g!=null && depth != 0) {
                g.setColor(Color.LIGHT_GRAY);
                g.setColor(colors[depth % colors.length]);
                g.setStroke(new BasicStroke(2));
                g.drawLine((int) points[i].posX, (int) points[i].posY, (int) points[i + 1].posX, (int) points[i + 1].posY);
                g.fillOval((int) points[i].posX-5, (int) points[i].posY-5, 10, 10);
                g.fillOval((int) points[i+1].posX-5, (int) points[i+1].posY-5, 10, 10);
            }
        }

        return deCasteljau(nextLevel, t, g, depth+1);
    }



    public double getXPos(double t) {
        double x  = 0;
        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        int n = PointList.getFirstPoint().length()-1;
        int k = 0;
        while (tempPoint != null) {
            x += binomalCo(n, k)*Math.pow((1-t), n-k)*Math.pow(t,k)*tempPoint.p.posX;
            k++;
            tempPoint = tempPoint.getNextPoint();
        }

        return x;
    }

    public double getXSpeed(double t) {
        double xSpd  = 0;
        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        int n = PointList.getFirstPoint().length()-1;
        int k = 0;
        while (tempPoint.getNextPoint() != null) {
            xSpd += binomalCo(n-1, k)*Math.pow((1-t), n-k-1)*Math.pow(t,k)*(tempPoint.getNextPoint().p.posX - tempPoint.p.posX);
            k++;
            tempPoint = tempPoint.getNextPoint();
        }

        return n*xSpd;
    }

    public double getXAcc(double t) {
        double xAcc  = 0;
        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        int n = PointList.getFirstPoint().length()-1;
        int k = 0;
        while (tempPoint.getNextPoint().getNextPoint() != null) {
            xAcc += binomalCo(n-2, k)*Math.pow((1-t), n-k-2)*Math.pow(t,k)*(tempPoint.getNextPoint().getNextPoint().p.posX - 2*tempPoint.getNextPoint().p.posX+tempPoint.p.posX);
            k++;
            tempPoint = tempPoint.getNextPoint();
        }

        return n*(n-1)*xAcc;
    }


    public double getYPos(double t) {
        double y  = 0;
        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        int n = PointList.getFirstPoint().length()-1;
        int k = 0;
        while (tempPoint != null) {
            y += binomalCo(n, k)*Math.pow((1-t), n-k)*Math.pow(t,k)*tempPoint.p.posY;
            k++;
            tempPoint = tempPoint.getNextPoint();
        }

        return y;
    }

    public double getYSpeed(double t) {
        double y  = 0;
        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        int n = PointList.getFirstPoint().length()-1;
        int k = 0;
        while (tempPoint.getNextPoint() != null) {
            y += binomalCo(n-1, k)*Math.pow((1-t), n-k-1)*Math.pow(t,k)*(tempPoint.getNextPoint().p.posY - tempPoint.p.posY);
            k++;
            tempPoint = tempPoint.getNextPoint();
        }

        return n*y;
    }

    public double getYAcc(double t) {
        double y  = 0;
        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        int n = PointList.getFirstPoint().length()-1;
        int k = 0;
        while (tempPoint.getNextPoint().getNextPoint() != null) {
            y += binomalCo(n-2, k)*Math.pow((1-t), n-k-2)*Math.pow(t,k)*(tempPoint.getNextPoint().getNextPoint().p.posY - 2*tempPoint.getNextPoint().p.posY+tempPoint.p.posY);
            k++;
            tempPoint = tempPoint.getNextPoint();
        }

        return n*(n-1)*y;
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
                        }catch (Exception ignored) {

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
        // newPoint.color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
        newPoint.color = colors[pointN % colors.length];
        pointN++;
        repaint();
    }

    private void setJToggleButton() {
        toggleButton = new JToggleButton("Vektor funktion");
        toggleButton.setBounds(-200,0, 100, 50);
        this.add(toggleButton);
    }
    private void SetAction() {
        toggleButton.addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(toggleButton.isSelected()) {
            toggleButton.setText("De casteljau's algoritme");
            repaint();
        }
        else {
            toggleButton.setText("Vektor funktion");
            repaint();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        double tVal = ((double)slider.getValue())/10000;
        label.setText("t = " + String.format("%.5f", tVal) + " v = " + String.format("%.3f", Math.sqrt(Math.pow(xSpeed,2) + Math.pow(ySpeed, 2))) + " a = " + String.format("%.3f", Math.sqrt(Math.pow(xAcc,2) + Math.pow(yAcc, 2))));
        repaint();
    }
}