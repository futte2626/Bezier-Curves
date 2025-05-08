import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;


import javax.swing.JPanel;
import javax.swing.Timer;

public class DrawPanel extends JPanel implements ActionListener, MouseListener {
    double prevX;
    double prevY;
    double xPos, yPos;
    volatile private boolean mouseDown = false;
    Point p0 = new Point(100, 200);
    Point p1 = new Point(300, 200);
    Point p2 = new Point(700, 600);
    Point p3 = new Point(400, 400);
    Point p4 = new Point(500, 500);
    Point p5 = new Point(600, 600);
    Point p6 = new Point(750, 750);
    Point[] points = {p0, p1, p2, p3, p4, p5, p6};


    DrawPanel() {
        this.setPreferredSize(new Dimension(800, 800));
        repaint();
        Timer timer = new Timer(10, this);
        timer.setRepeats(true);
        timer.start();
        addMouseListener(this);
        xPos = 1;
        yPos = 1;
        for(int i = 0 ; i < points.length; i++){
            System.out.println(binomalCoefficient(points.length-1, i));
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d =  (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        prevX = getXPos(0);
        prevY = getYPos(0);

        for (double t = 0; t <= 1; t += 0.0005) {
            xPos = getXPos(t);
            yPos = getYPos(t);


            g2d.setColor(new Color(255, 0, 0));
            g2d.setStroke(new BasicStroke(3));
            if (prevX != 0 && prevY != 0) {
                g2d.drawLine((int) prevX, (int) prevY, (int) xPos, (int) yPos);
            }


            prevX = xPos;
            prevY = yPos;
        }
        g2d.setColor(Color.BLACK);
        g2d.drawOval((int) (p0.getX() - 12.5), (int) (p0.getY() - 12.5), (int) 25, (int) 25);
        g2d.setColor(Color.BLUE);
        g2d.drawOval((int) (p1.getX() - 12.5), (int) (p1.getY() - 12.5), (int) 25, (int) 25);
        g2d.setColor(Color.GREEN);
        g2d.drawOval((int) (p2.getX() - 12.5), (int) (p2.getY() - 12.5), (int) 25, (int) 25);
        g2d.setColor(Color.YELLOW);
        g2d.drawOval((int) (p3.getX() - 12.5), (int) (p3.getY() - 12.5), (int) 25, (int) 25);
        g2d.setColor(Color.BLACK);
        g2d.drawOval((int) (p4.getX() - 12.5), (int) (p4.getY() - 12.5), (int) 25, (int) 25);
        g2d.setColor(Color.BLACK);
        g2d.drawOval((int) (p5.getX() - 12.5), (int) (p5.getY() - 12.5), (int) 25, (int) 25);
        g2d.setColor(Color.BLACK);
        g2d.drawOval((int) (p6.getX() - 12.5), (int) (p6.getY() - 12.5), (int) 25, (int) 25);




    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this.repaint();
    }

    public double getXPos(double t) {
        //return (Math.pow((1-t), 2)*p1.getX() + 2*(1-t)*t*p2.getX() + Math.pow(t, 2)*p3.getX());
       // return (Math.pow((1-t), 3)*p1.getX() + 3*Math.pow(1-t, 2)*t*p2.getX() + 3*(1-t)*Math.pow(t, 2)*p3.getX() + Math.pow(t, 3)*p4.getX());
        double x = 0;
        for (int i = 0; i < points.length; i++) {
            x += binomalCoefficient(points.length-1, i)*Math.pow((1-t), points.length-1-i)*Math.pow(t,i)*(points[i].getX());
        }
        return x;
    }

    public double getYPos(double t) {
        //return (Math.pow((1-t), 3)*p0.getY() + 3*Math.pow(1-t, 2)*t*p1.getY() + 3*(1-t)*Math.pow(t, 2)*p2.getY() + Math.pow(t, 3)*p3.getY());
        double y = 0;
        for (int i = 0; i < points.length; i++) {
            y += binomalCoefficient(points.length-1, i)*Math.pow((1-t), points.length-1-i)*Math.pow(t,i)*(points[i].getY());
        }
        return y;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseDown = true;
            Point mosPos = new Point(e.getX(), e.getY());
            if (mosPos.distance(p0) <= 15) initThread(p0);
            else if (mosPos.distance(p1) <= 15) initThread(p1);
            else if (mosPos.distance(p2) <= 15) initThread(p2);
            else if (mosPos.distance(p3) <= 15) initThread(p3);
            else if (mosPos.distance(p4) <= 15) initThread(p4);
            else if (mosPos.distance(p5) <= 15) initThread(p5);
            else if (mosPos.distance(p6) <= 15) initThread(p6);

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
                        p.posX = getMousePosition().x;
                        p.posY = getMousePosition().y;
                        repaint();
                    } while (mouseDown);
                    isRunning = false;
                }
            }.start();
        }
    }


}