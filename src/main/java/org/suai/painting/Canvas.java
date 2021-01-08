package org.suai.painting;

import org.suai.client.ClientWindow;
import org.suai.network.Message;
import org.suai.network.TCPConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * отвечает за рисование
 * отправляем сообщение серверу, при каждом изменении холста
 */

public class Canvas extends JComponent implements Serializable{

    private final String name;
    private BufferedImage image;
    private Color color = Color.black;
    private Graphics2D g2;
    private int currentX;
    private int currentY;
    private int oldX;
    private int oldY;
    private final TCPConnection connection;
    boolean test = false;

    public Canvas(String name, Image image, TCPConnection connection, ClientWindow window) {
        this.name = name;
        this.connection = connection;
        setDoubleBuffered(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();

                if (g2 != null) {
                    g2.drawLine(oldX, oldY, currentX, currentY);
                    repaint();
                    oldX = currentX;
                    oldY = currentY;
                }
            }
        });

        if (image == null) {
            this.image = null;
        } else {
            this.image = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr = this.image.createGraphics();
            bGr.drawImage(image, 0, 0, null);
            bGr.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        if (image == null ) {
            test = true;
            image = new BufferedImage(1200, 750, BufferedImage.TYPE_INT_RGB);
            g2 = (Graphics2D) image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            BasicStroke bs = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
            g2.setStroke(bs);
            clear();
        } else if (!test){
            test = true;
            g2 = (Graphics2D) image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            BasicStroke bs = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
            g2.setStroke(bs);
            g2.setPaint(color);
        }

        g.drawImage(image, 0, 0, null);

        if (image != null) {
            Message message = new Message("@sendToAllUpdateImage");
            message.setImage(name, image);
            connection.sendObject(message);
        }


    }

    public void updateImage(Image image) {
        System.out.println("Image is updating");
        color = g2.getColor();
        Graphics2D bGr = this.image.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();
        test = false;
    }

    public void clear() {
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
    }

    public void red() {
        g2.setPaint(Color.red);
    }

    public void black() {
        g2.setPaint(Color.black);
    }

    public void magenta() {
        g2.setPaint(Color.magenta);
    }

    public void green() {
        g2.setPaint(Color.green);
    }

    public void blue() {
        g2.setPaint(Color.blue);
    }

    public void yellow() {
        g2.setPaint(Color.yellow);
    }

    public void white() {
        g2.setPaint(Color.white);
    }

    public String getMyName() {
        return name;
    }

    public Image getImage() {
        return image;
    }
}
