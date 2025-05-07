import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class DrawPanel extends JPanel implements ActionListener {
    double prevX;
    double prevY;
    float t = 0;
    Point p1 = new Point(100, 700);
    Point p2 = new Point(300, 200);
    Point p3 = new Point(700, 600);

    DrawPanel() {
        this.setPreferredSize(new Dimension(800, 800));
        repaint();
        Timer timer = new Timer(1, this);
        timer.setRepeats(true);
        timer.start();

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d =  (Graphics2D) g;
        double xPos = getXPos();
        double yPos = getYPos();



        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(5));
        if(prevX != 0 && prevY != 0) g2d.drawLine((int)prevX, (int)prevY, (int)xPos, (int)yPos);
        double d = Math.sqrt(Math.pow((yPos - prevY), 2) + Math.pow((xPos- prevX), 2));
        System.out.println(d);



        g2d.setColor(Color.BLACK);
        g2d.drawOval((int)p1.getX()-5, (int)p1.getY()-5, (int)10, (int)10);
        g2d.setColor(Color.BLUE);
        g2d.drawOval((int)p2.getX()-5, (int)p2.getY()-5, (int)10, (int)10);
        g2d.setColor(Color.GREEN);
        g2d.drawOval((int)p3.getX()-5, (int)p3.getY()-5, (int)10, (int)10);

        if(t<1) t = t+0.01f;
        else {
            t-=1;
            t+=0.001f;
            xPos = getXPos();
            yPos = getYPos();
        }

        prevX = xPos;
        prevY = yPos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
    }

    public double getXPos() {
        return (Math.pow((1-t), 2)*p1.getX() + 2*(1-t)*t*p2.getX() + Math.pow(t, 2)*p3.getX());
    }

    public double getYPos() {
        return (Math.pow((1-t), 2)*p1.getY() + 2*(1-t)*t*p2.getY() + Math.pow(t, 2)*p3.getY());
    }
}