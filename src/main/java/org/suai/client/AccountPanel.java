package org.suai.client;

import org.suai.network.Account;
import org.suai.painting.Canvas;
import org.suai.painting.PaintCanvas;
import org.suai.painting.Painting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * отображает данные клиентского аккаунта
 * позволяет давать и отнимать доступ к своему аккаунту другим пользователлям
 */

public class AccountPanel extends JPanel implements ActionListener {

    private final JLabel name = new JLabel();
    private final ClientWindow window;
    private final Account account;

    private final JButton nextButton = new JButton(">>>");
    private final JButton exitButton = new JButton("ВЫХОД");
    private final JButton addAccessButton = new JButton("Дать доступ");
    private final JButton removeAccessButton = new JButton("Отменить доступ");
    private final JButton button = new JButton("Отправить");

    private final ArrayList<JButton> canvases = new ArrayList<>();
    private final ArrayList<String> canvasesNames = new ArrayList<>();
    private final JLabel canvasName = new JLabel();

    private final JTextField inputName = new JTextField();

    private final JLabel accesses = new JLabel();
    private int count = 0;

    public AccountPanel(ClientWindow window) {
        this.window = window;
        this.account = window.getMyAccount();

        setBackground(Color.pink);
        setLayout(null);

        name.setText(account.getUsername());
        name.setFont(new Font("Serif", Font.PLAIN, 30));
        name.setForeground(new Color(123,104, 238));
        name.setBounds(500, 50,200, 50);
        add(name);

        nextButton.addActionListener(this);
        nextButton.setFont(new Font("Serif", Font.PLAIN, 15));
        nextButton.setBackground(new Color(255, 242, 122));
        nextButton.setForeground(new Color(29, 10, 59));
        nextButton.setBounds(650, 650, 70, 50);
        add(nextButton);

        exitButton.addActionListener(this);
        exitButton.setFont(new Font("Serif", Font.PLAIN, 11));
        exitButton.setBackground(Color.pink);
        exitButton.setForeground(new Color(29, 10, 59));
        exitButton.setBounds(1000, 650, 100, 20);
        add(exitButton);

        canvasName.setFont(new Font("Serif", Font.PLAIN, 25));
        canvasName.setForeground(new Color(99, 20, 77));
        canvasName.setBounds(600, 200, 300, 50);

        for(int i = 0; i < account.getSize(); i++) {
            String string = account.getImageName(i);
            canvasesNames.add(string);

            if (account.getImage(string) == null) {
                System.out.println("image == 0");
            } else {
                JButton canvas = new JButton(new ImageIcon(account.getImage(string)));
                canvas.setBounds(100, 200, 400, 500);
                canvas.setBorder(BorderFactory.createEmptyBorder());
                canvas.setContentAreaFilled(false);
                canvas.setFocusable(false);

                canvas.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        setVisible(false);
                        org.suai.painting.Canvas canvas1 = new Canvas(string, account.getImage(string), window.getConnection(), window);
                        PaintCanvas paintCanvas = new PaintCanvas();

                        paintCanvas.show(canvas1, window);
                        Painting painting = new Painting(canvas1, window);
                        painting.start();
                    }
                });
                canvas.setVisible(false);
                canvases.add(canvas);
            }
        }

        if (canvases.size() != 0) {
            newCanvas();
        }

        addAccessButton.addActionListener(this);
        addAccessButton.setFont(new Font("Serif", Font.PLAIN, 15));
        addAccessButton.setBackground(Color.pink);
        addAccessButton.setForeground(new Color(29, 10, 59));
        addAccessButton.setBounds(910, 225, 200, 30);
        add(addAccessButton);

        removeAccessButton.addActionListener(this);
        removeAccessButton.setFont(new Font("Serif", Font.PLAIN, 15));
        removeAccessButton.setBackground(Color.pink);
        removeAccessButton.setForeground(new Color(29, 10, 59));
        removeAccessButton.setBounds(910, 255, 200, 30);
        add(removeAccessButton);

        inputName.setFont(new Font("Serif", Font.PLAIN, 20));
        inputName.setForeground(new Color(123,104, 238));
        inputName.setBackground(new Color(255, 242, 122));
        inputName.setBounds(600, 450,300, 50);

        button.setFont(new Font("Serif", Font.PLAIN, 20));
        button.setForeground(new Color(123,104, 238));
        button.addActionListener(this);
        button.setBounds(660, 500,150, 50);

        accesses.setFont(new Font("Serif", Font.PLAIN, 15));
        accesses.setForeground(new Color(99, 20, 77));
        accesses.setBounds(600, 250, 250, 600);
        accesses.setHorizontalAlignment(JLabel.LEFT);
        accesses.setVerticalAlignment(JLabel.TOP);
        updateList();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == nextButton && canvases.size() != 0) {
            newCanvas();

        } else if (e.getSource() == exitButton) {

            setVisible(false);
            Panel1 panel1 = new Panel1(window);
            panel1.setVisible(true);
            window.add(panel1);

        } else if (e.getSource() == addAccessButton) {

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    account.addAccess(inputName.getText());
                    updateList();
                    inputName.setText(null);
                    inputName.setVisible(false);
                    button.setVisible(false);
                    repaint();
                    button.removeActionListener(this);
                }
            });
            inputName.setVisible(true);
            button.setVisible(true);
            add(inputName);
            add(button);
            repaint();

        } else if (e.getSource() == removeAccessButton) {

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    account.removeAccess(inputName.getText());
                    updateList();
                    inputName.setText(null);
                    inputName.setVisible(false);
                    button.setVisible(false);
                    repaint();
                    button.removeActionListener(this);
                }
            });
            inputName.setVisible(true);
            button.setVisible(true);
            add(inputName);
            add(button);
            repaint();
        }
    }

    public void updateList() {
        ArrayList<String> accessesNames = account.getAccesses();

        String string = "<html>";
        for (int i = 0; i < accessesNames.size(); i++) {
            string += accessesNames.get(i) + "<br>";
        }
        string += "</html>";

        accesses.setText(string);
        add(accesses);
        repaint();
    }

    public void newCanvas() {
        JButton canvas;
        if (count != 0) {
            canvas = canvases.get(Math.abs((count - 1) % canvases.size()));
            canvas.setVisible(false);
            canvases.set((count - 1) % canvases.size(), canvas);
        }

        canvas = canvases.get(count % canvases.size());
        canvas.setVisible(true);
        canvases.set(count % canvases.size(), canvas);
        canvasName.setText(canvasesNames.get(count % canvases.size()));

        add(canvasName);
        add(canvas);

        count++;
        repaint();
    }
}
