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
    Point p1 = new Point(100, 200);
    Point p2 = new Point(300, 200);
    Point p3 = new Point(700, 600);


    DrawPanel() {
        this.setPreferredSize(new Dimension(800, 800));
        repaint();
        Timer timer = new Timer(10, this);
        timer.setRepeats(true);
        timer.start();
        addMouseListener(this);
        xPos = 1;
        yPos = 1;

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

            g2d.setColor(Color.BLACK);
            g2d.drawOval((int) (p1.getX() - 12.5), (int) (p1.getY() - 12.5), (int) 25, (int) 25);
            g2d.setColor(Color.BLUE);
            g2d.drawOval((int) (p2.getX() - 12.5), (int) (p2.getY() - 12.5), (int) 25, (int) 25);
            g2d.setColor(Color.GREEN);
            g2d.drawOval((int) (p3.getX() - 12.5), (int) (p3.getY() - 12.5), (int) 25, (int) 25);


            prevX = xPos;
            prevY = yPos;
        }



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // this.repaint();
    }

    public double getXPos(double t) {
        return (Math.pow((1-t), 2)*p1.getX() + 2*(1-t)*t*p2.getX() + Math.pow(t, 2)*p3.getX());
    }

    public double getYPos(double t) {
        return (Math.pow((1-t), 2)*p1.getY() + 2*(1-t)*t*p2.getY() + Math.pow(t, 2)*p3.getY());

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseDown = true;
            Point mosPos = new Point(e.getX(), e.getY());
            if (mosPos.distance(p1) <= 15) initThread(p1);
            else if (mosPos.distance(p2) <= 15) initThread(p2);
            else if (mosPos.distance(p3) <= 15) initThread(p3);
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