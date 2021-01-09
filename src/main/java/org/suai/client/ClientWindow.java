package org.suai.client;

import org.suai.network.Message;
import org.suai.network.TCPConnection;
import org.suai.network.TCPConnectionListener;
import org.suai.network.Account;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * подключается к серверу и общается с ним командами, описанными в классе Message
 * хранит и использует данные аккаунта
 * позвояет взаимодействовать всем пользовательским панелям и отвечает за их графическое представление
 */

public class ClientWindow extends JFrame implements TCPConnectionListener {

    private static final String IP_ADDR = "localhost";
    private static final int PORT = 1111;
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private String nickname = "Guest";
    private TCPConnection connection;
    private Account myAccount;
    private final HashMap<String, Image> imageArchive = new HashMap<>();

    private volatile boolean isRegistered;
    private volatile boolean responseIsReceived1;
    private volatile boolean updateImage;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private ClientWindow() {
        isRegistered = false;
        responseIsReceived1 = false;

        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            System.out.println("Connection exception: " + e);
        }

        RegistrationPanel registrationPanel = new RegistrationPanel(this);
        add(registrationPanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }

    @Override
    public void onReceiveObject(TCPConnection tcpConnection, Object in) {
        Message message = (Message) in;

        if(message.getCommand().equals("@yesYouCanCreate")) {
            responseIsReceived1 = true;
            isRegistered = true;

            myAccount.setImage(message.getImageName(), null);

            Message message1 = new Message("@newImage");
            message1.setUserName(nickname);
            message1.setImageName(message.getImageName());
            connection.sendObject(message1);

        } else if (message.getCommand().equals("@noYouCannotCreate")) {
            responseIsReceived1 = true;
            System.out.println("@noYouCannotCreate");

        } else if (message.getCommand().equals("@yesYouCanRegistered")) {
            System.out.println("@yesYouCanRegistered");
            responseIsReceived1 = true;
            isRegistered = true;
            sendMeImages();
            sendMeMyAcc();

        }else if (message.getCommand().equals("@noYouCannotRegistered")) {
            System.out.println("@yesYouCanRegistered");
            responseIsReceived1 = true;
            isRegistered = false;

        } else if (message.getCommand().equals("@newImage")) {

            imageArchive.put(message.getImageName(), message.getImage());

            if (message.getUserName().equals(nickname)) {
                myAccount.setImage(message.getImageName(), message.getImage());
            }
        } else if(message.getCommand().equals("@imagesArray")) {

            HashMap<String, ImageIcon> map = message.getImagesArray();
            for (String key: map.keySet()) {

                imageArchive.put(key, map.get(key).getImage());
            }
        } else if(message.getCommand().equals("@updateImage")) {

            imageArchive.put(message.getImageName(), message.getImage());
            for (int i = 0; i < myAccount.getSize(); i++) {

                if (myAccount.getImageName(i).equals(message.getImageName())) {
                    System.out.println("update image");
                    myAccount.setImage(message.getImageName(), message.getImage());
                }
            }
            updateImage = true;

        } else if (message.getCommand().equals("@yourImage")) {
            myAccount.setImage(message.getImageName(), message.getImage());

        } else if (message.getCommand().equals("@yourAcc")) {

            HashMap<String, ImageIcon> map = message.getImagesArray();
            for (String key: map.keySet()) {

                myAccount.setImage(key, map.get(key).getImage());
            }
            myAccount.setAccesses(message.getAccesses());

        } else if (message.getCommand().equals("@noYouCannotPainting")) {
            responseIsReceived1 = true;
            isRegistered = false;

        } else if (message.getCommand().equals("@yesYouCanPainting")) {
            responseIsReceived1 = true;
            isRegistered = true;
        }
    }

    public boolean getIsRegistered() {
        return isRegistered;
    }

    public boolean getUpdateImage() {
        return updateImage;
    }

    public HashMap<String, Image> getImageArchive() {
        return imageArchive;
    }

    public String getNickname() {
        return nickname;
    }

    public Account getMyAccount() {
        return myAccount;
    }

    public TCPConnection getConnection() {
        return connection;
    }

    public boolean getResponseIsReceived1() {
        return responseIsReceived1;
    }

    public Image getImage(String imageName) {
        return imageArchive.get(imageName);
    }

    public void createCanvas(String canvasName) {
        isRegistered = false;
        responseIsReceived1 = false;

        Message message = new Message("@canICreate");
        message.setImageName(canvasName);
        message.setUserName(nickname);
        connection.sendObject(message);
    }

    public void paintingInCanvas(String userName, String canvasName) {
        Message message = new Message("@canIPainting");
        message.setUserName(userName);
        message.setImageName(canvasName);
        connection.sendObject(message);
    }

    public void sendMeImages() {
        Message message = new Message("@sendMeImagesArray");
        connection.sendObject(message);
    }

    public void sendMeMyAcc() {
        Message message = new Message("@sendMeMyAcc");
        message.setUserName(nickname);
        connection.sendObject(message);
    }

    public void setMyAccount(Account account) {
        myAccount = account;
        nickname = myAccount.getUsername();
    }

    public void setRegistered(boolean registered) {
        this.isRegistered = registered;
    }

    public void setUpdateImage(boolean b) {
        updateImage = b;
    }

    public void setResponseIsReceived1(boolean response) {
        responseIsReceived1 = response;
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        System.out.println("Connection ready...");
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        System.out.println("Connection close...");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }
}
