package org.suai.network;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Хранит все данные акаунта (имя, пароль, холсты, доступы, соединение с сервером)
 */

public class Account {

    private final String username;
    private final String password;
    private final HashMap<String, Image> images = new HashMap<>();
    private final ArrayList<String> imagesNames = new ArrayList<>();
    private ArrayList<String> accesses = new ArrayList<>();
    private final TCPConnection connection;

    public Account(String username, String password, TCPConnection connection) {

        Message message = new Message("@canIRegistered");
        message.setUserName(username);
        message.setPassword(password);
        connection.sendObject(message);

        this.connection = connection;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getSize() {
        return images.size();
    }

    public Image getImage(String canvasName) {

        return images.get(canvasName);
    }

    public void setImage(String canvasName, Image image) {

        for (int i = 0; i < imagesNames.size(); i++) {

            if (imagesNames.get(i).equals(canvasName)) {
                images.put(canvasName, image);
                return;
            }
        }
        imagesNames.add(canvasName);
        images.put(canvasName, image);
    }

    public String getImageName(int index) {
        return imagesNames.get(index);
    }

    public ArrayList<String> getAccesses() {
        return accesses;
    }

    public void setAccesses(ArrayList<String> accesses) {
        this.accesses = accesses;
    }

    public void addAccess(String name) {

        accesses.add(name);
        Message message = new Message("@iAddAccess");
        message.setUserName(username);
        message.setAccess(name);
        connection.sendObject(message);
        System.out.println("iAddAccess: " + name);
    }

    public void removeAccess(String name) {

        for (int i = 0; i < accesses.size(); i++) {

            if (accesses.get(i).equals(name)) {

                accesses.remove(i);
                Message message = new Message("@iRemoveAccess");
                message.setUserName(username);
                message.setAccess(name);
                connection.sendObject(message);
                System.out.println("iRemoveAccess: " + name);
                return;
            }
        }
    }
}
