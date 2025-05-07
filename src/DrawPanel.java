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
    Point p1 = new Point(0, 800);
    Point p2 = new Point(0, 0);
    Point p3 = new Point(800, 800);

    DrawPanel() {
        this.setPreferredSize(new Dimension(800, 800));
        Timer timer = new Timer(1, this);
        timer.setRepeats(true);
        timer.start();

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d =  (Graphics2D) g;
        double xPos = (Math.pow((1-t), 2)*p1.getX() + 2*(1-t)*t*p2.getX() + Math.pow(t, 2)*p3.getX());
        double yPos = (Math.pow((1-t), 2)*p1.getY() + 2*(1-t)*t*p2.getY() + Math.pow(t, 2)*p3.getY());



        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(2));
        if(prevX != 0 && prevY != 0) g2d.drawLine((int)prevX, (int)prevY, (int)xPos, (int)yPos);
        double d = Math.sqrt(Math.pow((yPos - prevY), 2) + Math.pow((xPos- prevX), 2));
        System.out.println(d);

        prevX = xPos;
        prevY = yPos;


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(t<1) t = t+0.01f;
        this.repaint();
    }
}