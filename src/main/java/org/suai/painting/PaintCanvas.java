package org.suai.painting;

import org.suai.client.ClientWindow;
import org.suai.client.Panel1;
import org.suai.network.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * отвечает за отображение данных холста
 * позволяет выбрать и пременить операцию к холсту
 */

public class PaintCanvas extends JFrame implements ActionListener{

    private final JButton clearBth = new JButton("Clear");
    private final JButton exitBth = new JButton("ВЫХОД");
    private final JButton blackBth = new JButton();
    private final JButton blueBth = new JButton();
    private final JButton greenBth = new JButton();
    private final JButton redBth = new JButton();
    private final JButton magentaBth = new JButton();
    private final JButton yellowBth = new JButton();
    private final JButton whiteBth = new JButton();
    private ClientWindow window;
    private Canvas canvas;

    public void show(Canvas canvas, ClientWindow window) {
        this.window = window;
        window.setVisible(false);
        this.canvas = canvas;

        setTitle("PAINTING");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(canvas, BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.setBackground(Color.pink);
        clearBth.setFont(new Font("Serif", Font.PLAIN, 10));
        clearBth.addActionListener(this);
        blackBth.setBackground(Color.black);
        blackBth.addActionListener(this);
        blueBth.setBackground(Color.blue);
        blueBth.addActionListener(this);
        greenBth.setBackground(Color.green);
        greenBth.addActionListener(this);
        redBth.setBackground(Color.red);
        redBth.addActionListener(this);
        magentaBth.setBackground(Color.magenta);
        magentaBth.addActionListener(this);
        yellowBth.setBackground(Color.yellow);
        yellowBth.addActionListener(this);
        whiteBth.setBackground(Color.white);
        whiteBth.addActionListener(this);

        exitBth.addActionListener(this);
        exitBth.setFont(new Font("Serif", Font.PLAIN, 10));
        exitBth.setBackground(Color.pink);
        exitBth.setForeground(new Color(29, 10, 59));

        panel.add(blackBth);
        panel.add(blueBth);
        panel.add(greenBth);
        panel.add(redBth);
        panel.add(magentaBth);
        panel.add(yellowBth);
        panel.add(whiteBth);
        panel.add(clearBth);
        panel.add(exitBth);

        panel.setVisible(true);
        container.add(panel, BorderLayout.SOUTH);
        container.setVisible(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == clearBth) {
            canvas.clear();
        } else if (e.getSource() == blackBth) {
            canvas.black();
        } else if (e.getSource() == blueBth) {
            canvas.blue();
        } else if (e.getSource() == greenBth) {
            canvas.green();
        } else if (e.getSource() == redBth) {
            canvas.red();
        } else if (e.getSource() == magentaBth) {
            canvas.magenta();
        } else if (e.getSource() == yellowBth) {
            canvas.yellow();
        } else if (e.getSource() == whiteBth) {
            canvas.white();
        } else if (e.getSource() == exitBth) {
            setVisible(false);
            Message message = new Message("@sendToAllUpdateImage");
            message.setImage(canvas.getMyName(), canvas.getImage());
            window.getConnection().sendObject(message);
            Panel1 panel1 = new Panel1(window);
            panel1.setVisible(true);
            window.add(panel1);
            window.setVisible(true);
        }
    }

}
