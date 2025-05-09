import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.Random;


import javax.swing.JPanel;
import javax.swing.Timer;

public class DrawPanel extends JPanel implements ActionListener, MouseListener {
    double prevX;
    double prevY;
    double xPos, yPos;
    volatile private boolean mouseDown = false;



    DrawPanel() {
        this.setPreferredSize(new Dimension(1000, 800));
        repaint();
        Timer timer = new Timer(10, this);
        timer.setRepeats(true);
        timer.start();
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

        for (double t = 0; t <= 1; t += 0.00005) {
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
      /*  g2d.setColor(Color.BLACK);
        g2d.drawOval((int) (p0.getX() - 12.5), (int) (p0.getY() - 12.5), (int) 25, (int) 25);
        g2d.setColor(Color.BLUE);
        g2d.drawOval((int) (p1.getX() - 12.5), (int) (p1.getY() - 12.5), (int) 25, (int) 25);
        g2d.setColor(Color.GREEN);
        g2d.drawOval((int) (p2.getX() - 12.5), (int) (p2.getY() - 12.5), (int) 25, (int) 25);
        g2d.setColor(Color.YELLOW);
        g2d.drawOval((int) (p3.getX() - 12.5), (int) (p3.getY() - 12.5), (int) 25, (int) 25);
 */
        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        while (tempPoint != null) {
            g2d.setColor(tempPoint.color);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(tempPoint.p.posX - 12, tempPoint.p.posY -12, 25, 25);
            g2d.setColor(Color.gray);
            g2d.setStroke(new BasicStroke(1));
            if(tempPoint.getNextPoint() != null) g2d.drawLine(tempPoint.p.posX, tempPoint.p.posY, tempPoint.getNextPoint().p.posX, tempPoint.getNextPoint().p.posY);
            tempPoint = tempPoint.getNextPoint();
        }






    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this.repaint();
    }

    public double getXPos(double t) {
        //return (Math.pow((1-t), 2)*p1.getX() + 2*(1-t)*t*p2.getX() + Math.pow(t, 2)*p3.getX());
       // return (Math.pow((1-t), 3)*p1.getX() + 3*Math.pow(1-t, 2)*t*p2.getX() + 3*(1-t)*Math.pow(t, 2)*p3.getX() + Math.pow(t, 3)*p4.getX());

        /*double x = 0;
        for (int i = 0; i < points.length; i++) {
            x += binomalCoefficient(points.length-1, i)*Math.pow((1-t), points.length-1-i)*Math.pow(t,i)*(points[i].getX());
        }
        return x; */

        double x  = 0;

        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        int listLength = PointList.getFirstPoint().length();
        int pointNumber = 0;
        while (tempPoint != null) {
            x += binomalCoefficient(listLength - 1, pointNumber)*Math.pow((1-t), listLength-1-pointNumber)*Math.pow(t,pointNumber)*(tempPoint.p.getX());
            pointNumber++;
            tempPoint = tempPoint.getNextPoint();
        }


        return x;
    }

    public double getYPos(double t) {
        //return (Math.pow((1-t), 3)*p0.getY() + 3*Math.pow(1-t, 2)*t*p1.getY() + 3*(1-t)*Math.pow(t, 2)*p2.getY() + Math.pow(t, 3)*p3.getY());
       /* double y = 0;
        for (int i = 0; i < points.length; i++) {
            y += binomalCoefficient(points.length-1, i)*Math.pow((1-t), points.length-1-i)*Math.pow(t,i)*(points[i].getY());
        }
        return y; */
        double y  = 0;

        PointList tempPoint;
        tempPoint = PointList.getFirstPoint();
        int listLength = PointList.getFirstPoint().length();
        int pointNumber = 0;
        while (tempPoint != null) {
            y += binomalCoefficient(listLength - 1, pointNumber)*Math.pow((1-t), listLength-1-pointNumber)*Math.pow(t,pointNumber)*(tempPoint.p.getY());
            pointNumber++;
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