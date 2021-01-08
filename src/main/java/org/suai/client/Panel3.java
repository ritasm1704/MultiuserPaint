package org.suai.client;

import org.suai.painting.Canvas;
import org.suai.painting.PaintCanvas;
import org.suai.painting.Painting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * позволяет создать холст, если холста с таким именем нет на сервере
 */

public class Panel3 extends JPanel implements ActionListener {

    private final JButton sendButton = new JButton("Отправить");
    private final JLabel text = new JLabel("Такой холст уже существует, придумайте другое имя :)");
    private final JButton exitButton = new JButton("ВЫХОД");

    private final JTextField inputName = new JTextField();
    private final ClientWindow window;

    public Panel3(ClientWindow window) {
        this.window = window;
        setBackground(Color.pink);
        setLayout(null);

        JLabel text1 = new JLabel("Введите имя холста");
        text1.setFont(new Font("Serif", Font.PLAIN, 30));
        text1.setBounds(400, 200,400, 50);
        text1.setForeground(new Color(123,104, 238));

        inputName.setFont(new Font("Serif", Font.PLAIN, 30));
        inputName.setBounds(400, 300,400, 50);
        inputName.setForeground(new Color(123,104, 238));
        inputName.setBackground(new Color(255, 242, 122));

        text.setFont(new Font("Serif", Font.PLAIN, 25));
        text.setForeground(Color.red);
        text.setBounds(400, 240,600, 50);
        text.setVisible(false);

        sendButton.setFont(new Font("Serif", Font.PLAIN, 20));
        sendButton.setBounds(400, 600,150, 50);
        sendButton.setForeground(new Color(123,104, 238));
        sendButton.addActionListener(this);

        exitButton.addActionListener(this);
        exitButton.setFont(new Font("Serif", Font.PLAIN, 11));
        exitButton.setBackground(Color.pink);
        exitButton.setForeground(new Color(29, 10, 59));
        exitButton.setBounds(1000, 650, 100, 20);

        add(text1);
        add(inputName);
        add(text);
        add(sendButton);
        add(exitButton);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        window.setRegistered(false);
        window.setResponseIsReceived1(false);

        if (e.getSource() == sendButton) {

            String string = inputName.getText();
            window.createCanvas(string);

            while (!window.getResponseIsReceived1()) {
                System.out.print(window.getResponseIsReceived1());
            }
            System.out.println(window.getResponseIsReceived1());

            if (!window.getIsRegistered()) {

                inputName.setText(null);
                text.setVisible(true);
            } else {
                setVisible(false);
                org.suai.painting.Canvas canvas1 = new Canvas(string, null, window.getConnection(), window);
                PaintCanvas paintCanvas = new PaintCanvas();
                paintCanvas.show(canvas1, window);
                Painting painting = new Painting(canvas1, window);
                painting.start();
            }
            window.setResponseIsReceived1(false);

        } else if (e.getSource() == exitButton) {

            setVisible(false);
            Panel1 panel1 = new Panel1(window);
            panel1.setVisible(true);
            window.add(panel1);
        }
    }
}
