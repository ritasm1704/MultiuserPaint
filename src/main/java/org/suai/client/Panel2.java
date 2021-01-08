package org.suai.client;

import org.suai.painting.Canvas;
import org.suai.painting.PaintCanvas;
import org.suai.painting.Painting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Отображает все существующие холсты и позволяет к ним присоединиться,
 * если есть доступ
 */

public class Panel2 extends JPanel implements ActionListener {

    private final JButton exitButton = new JButton("ВЫХОД");
    private final JButton nextButton = new JButton(">>>");
    private final JButton refreshButton = new JButton("обновить");
    private final JLabel accessLabel = new JLabel("У вас нет доступа((");

    private final ArrayList<JButton> canvases = new ArrayList<>();
    private final ArrayList<String> canvasesNames = new ArrayList<>();
    private HashMap<String, Image> imageArchive;
    private final JLabel canvasName = new JLabel();
    private final ClientWindow window;
    private int count = 0;

    public Panel2(ClientWindow window) {

        this.window = window;
        imageArchive = window.getImageArchive();
        setBackground(Color.pink);
        setLayout(null);

        JLabel label = new JLabel("Выберите холст");
        label.setFont(new Font("Serif", Font.PLAIN, 50));
        label.setForeground(new Color(123,104, 238));
        label.setBounds(350, 100, 500, 70);
        add(label);

        exitButton.addActionListener(this);
        exitButton.setFont(new Font("Serif", Font.PLAIN, 11));
        exitButton.setBackground(Color.pink);
        exitButton.setForeground(new Color(29, 10, 59));
        exitButton.setBounds(1000, 650, 100, 20);
        add(exitButton);

        nextButton.addActionListener(this);
        nextButton.setFont(new Font("Serif", Font.PLAIN, 15));
        nextButton.setBackground(new Color(255, 242, 122));
        nextButton.setForeground(new Color(29, 10, 59));
        nextButton.setBounds(480, 650, 70, 50);
        add(nextButton);

        refreshButton.addActionListener(this);
        refreshButton.setFont(new Font("Serif", Font.PLAIN, 11));
        refreshButton.setBackground(Color.pink);
        refreshButton.setForeground(new Color(29, 10, 59));
        refreshButton.setBounds(100, 650, 100, 20);
        add(refreshButton);

        accessLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        accessLabel.setForeground(Color.red);
        accessLabel.setBounds(400, 600,400, 50);
        accessLabel.setVisible(false);

        canvasName.setFont(new Font("Serif", Font.PLAIN, 25));
        canvasName.setForeground(new Color(99, 20, 77));
        canvasName.setBounds(450, 200, 400, 50);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        window.setResponseIsReceived1(false);
        window.setRegistered(false);

        if (e.getSource() == exitButton) {

            setVisible(false);
            Panel1 panel1 = new Panel1(window);
            panel1.setVisible(true);
            window.add(panel1);

        } else if (e.getSource() == nextButton && imageArchive.size() != 0) {

            JButton canvas = canvases.get((count - 1) % canvases.size());
            canvas.setVisible(false);
            canvases.set((count - 1) % canvases.size(), canvas);

            canvas = canvases.get(count % canvases.size());
            canvas.setVisible(true);
            canvases.set(count % canvases.size(), canvas);
            canvasName.setText(canvasesNames.get(count % canvases.size()));
            add(canvas);
            count++;

        } else if (e.getSource() == refreshButton) {

            accessLabel.setVisible(false);
            imageArchive = window.getImageArchive();
            window.sendMeImages();

            for (String key: imageArchive.keySet()) {

                if (imageArchive.get(key) == null) {
                    System.out.println("image == 0");
                } else {
                    canvasesNames.add(key);
                    JButton canvas = new JButton(new ImageIcon(imageArchive.get(key)));
                    canvas.setBounds(175, 250, 800, 350);
                    canvas.setBorder(BorderFactory.createEmptyBorder());
                    canvas.setContentAreaFilled(false);
                    canvas.setFocusable(false);

                    canvas.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                            window.setResponseIsReceived1(false);
                            window.setRegistered(false);
                            window.paintingInCanvas(window.getNickname(), key);

                            while (!window.getResponseIsReceived1()) {
                                System.out.print(window.getResponseIsReceived1());
                            }
                            System.out.println(window.getResponseIsReceived1());

                            if (window.getIsRegistered()) {
                                setVisible(false);
                                org.suai.painting.Canvas canvas1 = new Canvas(key, imageArchive.get(key), window.getConnection(), window);
                                PaintCanvas paintCanvas = new PaintCanvas();

                                paintCanvas.show(canvas1, window);
                                Painting painting = new Painting(canvas1, window);
                                painting.start();
                            } else {
                                accessLabel.setVisible(true);
                                add(accessLabel);
                                repaint();
                            }
                        }
                    });
                    canvas.setVisible(false);
                    canvases.add(canvas);
                }

                if (canvases.size() != 0) {
                    count = 0;

                    JButton canvas = canvases.get(count % canvases.size());
                    canvas.setVisible(true);
                    canvases.set(count % canvases.size(), canvas);
                    canvasName.setText(canvasesNames.get(count % canvases.size()));

                    add(canvasName);
                    add(canvas);

                    count++;
                    repaint();
                }
            }
        }
    }
}
