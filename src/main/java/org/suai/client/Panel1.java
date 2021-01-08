package org.suai.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * главная панель
 * с нее можно перейти на панель со всеми созданными холстами,
 *                               с возможностью создать свой холст
 *                               с возможностью посмотреть свой аккаунт
 */

public class Panel1 extends JPanel implements ActionListener {

    private final JButton connectionButton = new JButton("Присоединиться");
    private final JButton createButton = new JButton("Создать");
    private final JButton myAccountButton = new JButton("Мой аккаунт");
    private final ClientWindow window;

    public Panel1(ClientWindow window) {

        this.window = window;
        setBackground(Color.pink);
        setLayout(null);

        JLabel label = new JLabel("MULTIUSER PAINT");
        label.setFont(new Font("Serif", Font.PLAIN, 50));
        label.setForeground(new Color(123,104, 238));
        label.setBounds(350, 200, 500, 50);

        JLabel label2 = new JLabel("Hi, " + window.getNickname() + ", go to");
        label2.setFont(new Font("Serif", Font.PLAIN, 30));
        label2.setForeground(new Color(123,104, 238));
        label2.setBounds(200, 150, 500, 50);

        connectionButton.addActionListener(this);
        connectionButton.setFont(new Font("Serif", Font.PLAIN, 20));
        connectionButton.setForeground(new Color(123,104, 238));
        connectionButton.setBounds(200, 400, 300, 100);

        createButton.addActionListener(this);
        createButton.setFont(new Font("Serif", Font.PLAIN, 20));
        createButton.setForeground(new Color(123,104, 238));
        createButton.setBounds(600, 400, 300, 100);

        myAccountButton.addActionListener(this);
        myAccountButton.setFont(new Font("Serif", Font.PLAIN, 20));
        myAccountButton.setForeground(new Color(123,104, 238));
        myAccountButton.setBounds(200, 550, 200, 50);

        add(label);
        add(label2);
        add(connectionButton);
        add(createButton);
        add(myAccountButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == connectionButton) {

            setVisible(false);
            window.sendMeImages();
            Panel2 panel2 = new Panel2(window);
            window.add(panel2);

        } else if (e.getSource() == createButton) {

            setVisible(false);
            Panel3 panel3 = new Panel3(window);
            window.add(panel3);

        } else if (e.getSource() == myAccountButton) {

            setVisible(false);
            AccountPanel accountPanel = new AccountPanel(window);
            window.add(accountPanel);
        }
    }
}
