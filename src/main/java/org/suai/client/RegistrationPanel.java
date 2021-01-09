package org.suai.client;

import org.suai.network.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * отображается самой первой
 * позволяет зарегестрироваться и войти в свой аккаунт
 */

public class RegistrationPanel extends JPanel implements ActionListener {

    private final JLabel text = new JLabel("Такое имя уже существует/неверный пароль");
    private final JTextField inputName = new JTextField();
    private final JPasswordField inputPassword = new JPasswordField();
    private final JButton button1 = new JButton("видимость");
    private final JButton button2 = new JButton("Отправить");
    private final ClientWindow window;
    private int click = 0;

    public RegistrationPanel(ClientWindow window) {
        this.window = window;
        setBackground(Color.pink);
        setLayout(null);

        text.setFont(new Font("Serif", Font.PLAIN, 25));
        text.setForeground(Color.red);
        text.setBounds(400, 240,500, 50);
        text.setVisible(false);

        JLabel text1 = new JLabel("Введите свое имя");
        text1.setFont(new Font("Serif", Font.PLAIN, 30));
        text1.setBounds(400, 200,400, 50);
        text1.setForeground(new Color(123,104, 238));

        inputName.setFont(new Font("Serif", Font.PLAIN, 30));
        inputName.setBounds(400, 300,400, 50);
        inputName.setForeground(new Color(123,104, 238));
        inputName.setBackground(new Color(255, 242, 122));

        JLabel text2 = new JLabel("Введите свой пароль");
        text2.setFont(new Font("Serif", Font.PLAIN, 30));
        text2.setBounds(400, 400,400, 50);
        text2.setForeground(new Color(123,104, 238));

        inputPassword.setFont(new Font("Serif", Font.PLAIN, 30));
        inputPassword.setBounds(400, 500,400, 50);
        inputPassword.setForeground(new Color(123,104, 238));
        inputPassword.setBackground(new Color(255, 242, 122));

        button1.setFont(new Font("Serif", Font.PLAIN, 20));
        button1.setBounds(810, 500,150, 50);
        button1.setForeground(new Color(123,104, 238));
        button1.addActionListener(this);

        button2.setFont(new Font("Serif", Font.PLAIN, 20));
        button2.setBounds(400, 600,150, 50);
        button2.setForeground(new Color(123,104, 238));
        button2.addActionListener(this);

        add(text1);
        add(inputName);
        add(text2);
        add(inputPassword);
        add(button1);
        add(button2);
        add(text);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button1) {

            if (click % 2 == 0) {
                inputPassword.setEchoChar((char) 0);
            } else {
                inputPassword.setEchoChar('•');
            }
            click++;

        } else if (e.getSource() == button2) {
            window.setResponseIsReceived1(false);

            Account account = new Account(inputName.getText(), String.valueOf(inputPassword.getPassword()), window.getConnection());
            while (!window.getResponseIsReceived1()) {
                System.out.print(window.getResponseIsReceived1());
            }
            System.out.println(window.getResponseIsReceived1());

            if (!window.getIsRegistered()) {

                inputName.setText(null);
                inputPassword.setText(null);
                text.setVisible(true);
            } else {
                window.setMyAccount(account);
                setVisible(false);
                Panel1 panel1 = new Panel1(window);
                window.add(panel1);
            }
            window.setResponseIsReceived1(false);
        }
    }
}
